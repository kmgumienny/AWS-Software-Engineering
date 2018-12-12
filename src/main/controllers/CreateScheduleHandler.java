package main.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import main.database.ScheduleDAO;
import main.database.TimeslotDAO;
import main.entities.Schedule;
import main.entities.Timeslot;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class CreateScheduleHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;
	String status = "OK";

	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to create new schedule");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CreateScheduleResponse response = null;
		
		// extract body from incoming HTTP POST request. If any error, then return 422 error
		String body;
		boolean processed = false;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());
			
			String method = (String) event.get("httpMethod");
			if (method != null && method.equalsIgnoreCase("OPTIONS")) {
				logger.log("Options request");
				response = new CreateScheduleResponse("name", 200);  // OPTIONS needs a 200 response
		        responseJson.put("body", new Gson().toJson(response));
		        processed = true;
		        body = null;
			} else {
				body = (String)event.get("body");
				if (body == null) {
					body = event.toJSONString();  // this is only here to make testing easier
				}
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new CreateScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			//Create new request by tamplet
			CreateScheduleRequest req = new Gson().fromJson(body, CreateScheduleRequest.class);
			logger.log(req.toString());
			status = "OK";
			
			//Create the new schedule
			Schedule newSchedule = createSchedule(req.scheduleName, req.startDate, req.endDate, req.dailyStartTime, req.dailyEndTime, req.timeSlotDuration);
			
			if(status.equals("Something went wrong and request failed to exicute. Please recreate the schedule.")){
				CreateScheduleResponse resp = new CreateScheduleResponse(status, 500);
				responseJson.put("body", new Gson().toJson(resp));  
			}
			else {
				//Create the response for the new schedule
				CreateScheduleResponse resp = new CreateScheduleResponse("Schedule sucessifully created.", newSchedule.getScheduleID(), newSchedule.getSecretCode());
				responseJson.put("body", new Gson().toJson(resp));  
			}
		}
		
		//return the result
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
	}
	
	
///////////////////////////////// Milap Code edition Working 
	
	Schedule createSchedule(String scheduleName, String startDate, String endDate, int dayStartTime, int dayEndTime, int timeSlotDuration) {
		if (logger != null) { logger.log("in createSchedule"); }
		ScheduleDAO dao = new ScheduleDAO();
		
		
		//date of creation
		LocalDateTime creationDate = LocalDateTime.now();
		
		//create new schedule only
		Schedule schedule = new Schedule(scheduleName, startDate, endDate, dayStartTime, dayEndTime, timeSlotDuration, creationDate);
		
		//try to add to the RDS schedule table
		try {
			boolean ans = dao.addSchedule(schedule);
		} catch (Exception e) {
			logger.log("Failed to add schedule to the RDS's Schedule Table");
			status = "Something went wrong and request failed to exicute. Please recreate the schedule.";
		}
		
		//Create time slots for the newly created schedule
		createTimeSlots(schedule.getScheduleID(),  schedule.getStartDate(), schedule.getEndDate(), schedule.getDayStartTime(), schedule.getDayEndTime(), schedule.getTimeSlotDuration());
		
		return schedule;
	}
	
	
	
	void createTimeSlots(String scheduleID, LocalDate startDate, LocalDate endDate, int startTime, int endTime, int duration) {
		
		//make connection to the RDS timeslot table
		TimeslotDAO tdao = new TimeslotDAO(); 
		
		//Calculate the different paramaters
		long dailyTime = (endTime - startTime)*60;
		long numTimeslotsPerDay = dailyTime/duration;
		long numDays= ChronoUnit.DAYS.between(startDate, endDate);
		
		//check if the start date and adjust the starting week if needed
		int currentWeek = 1;
		if(startDate.getDayOfWeek().name() == "MONDAY" || startDate.getDayOfWeek().name() == "SATURDAY" || startDate.getDayOfWeek().name() == "SUNDAY") {
			currentWeek = 0;
		}
		
		//create variables
		LocalDate itterationDate = startDate;
		LocalTime sTime = LocalTime.of(startTime, 0);
		int currentSlotNum;
		int currentDayOfWeek;
		
		//Loop through and create the time slots for each day
		for (int i = 0; i <= (int) numDays; i++)
		{
			//If Monday, time slots are added to a new week, increment the week counter
			if(itterationDate.getDayOfWeek().name() == "MONDAY") {
				currentWeek = currentWeek + 1;
			}
			
			//check if the current day is Saturday or Sunday. If so skip those days
			if (itterationDate.getDayOfWeek().name() == "SATURDAY" || itterationDate.getDayOfWeek().name() == "SUNDAY")
				itterationDate = itterationDate.plusDays(1);
			else {
				//Reset the time slot number counter for day for every new date
				currentSlotNum = 1;
				
				//Loop through and create time slots for the given time frame
				for (long j = 0; j < numTimeslotsPerDay; j++)
				{
					//Get the current day of the week in int
					currentDayOfWeek = itterationDate.getDayOfWeek().getValue();
					
					//Create new time slot
					Timeslot ts = new Timeslot(scheduleID, currentWeek, currentDayOfWeek, currentSlotNum, LocalDateTime.of(itterationDate, sTime), false, true);
					
					//Try to add the time slot to the RDS timeSlot table
					try {
						boolean ans = tdao.addTimeslot(ts);
					} catch (Exception e) {
						logger.log("Failed to add timeslot to the RDS's TimeSlot Table");
						status = "Something went wrong and request failed to exicute. Please recreate the schedule.";
					}
					
					//Inctiment the new start time for next time slot and the slot number for the day
					sTime = sTime.plusMinutes(duration);
					currentSlotNum = currentSlotNum + 1;
				}
				
				//Inciment the date by one day and reset the start time for the next day to specified time
				itterationDate = itterationDate.plusDays(1);
				sTime = LocalTime.of(startTime, 0); 
			}
		}
	}

}
