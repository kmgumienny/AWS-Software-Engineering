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
import java.time.format.DateTimeFormatter;
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
public class ExtandEndDateHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;
	String status = "OK";

	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
	
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to extand schedule end date");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		ExtandEndDateResponse response = null;
		
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
				response = new ExtandEndDateResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new ExtandEndDateResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			ExtandEndDateRequest req = new Gson().fromJson(body, ExtandEndDateRequest.class);
			logger.log(req.toString());
			status = "OK";
			
			LocalDate newDate = parseDate(req.newDate);
			
			extandScheduleEndingDate(req.scheduleID, req.originizerSecretCode, newDate);
			
			//Response creation
			if(status.equals("OK")){
				response = new ExtandEndDateResponse("End date for schedule applied and updated successifully.");
		        responseJson.put("body", new Gson().toJson(response));
			}
			else if(status.equals("Something went wrong and request failed to exicute. Please retry")) {
				
				response = new ExtandEndDateResponse(status, 500);
		        responseJson.put("body", new Gson().toJson(response));
			}
			else {
				response = new ExtandEndDateResponse(status, 422);
		        responseJson.put("body", new Gson().toJson(response));
			}
		}
		
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
	}
	
	
////////////////////////////////////////////////////////////////////////////////////
	
	void extandScheduleEndingDate(String scheduleID, String originizerSecretCode, LocalDate newDate) {
		TimeslotDAO timeSlotDAO = new TimeslotDAO();
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		boolean timeExists = false;
		boolean worked = true;
		Schedule schedule = null;

		try {
			schedule = scheduleDAO.getSchedule(scheduleID);
		} catch (Exception e) {
			logger.log("Schedule does not exist with provided schedule ID.");
			status = "Schedule does not exist with provided schedule ID.";
		}

		if(schedule != null) {
			if(schedule.getSecretCode().equals(originizerSecretCode)) {
				if(newDate.isAfter(schedule.getEndDate())) {
					LocalDate startDate = schedule.getStartDate();
					int startTime = schedule.getDayStartTime();
					int endTime = schedule.getDayEndTime();
					int duration = schedule.getTimeSlotDuration();
					createTimeSlots(scheduleID, startDate, newDate, startTime, endTime, duration);
					if(!status.equals("Something went wrong and request failed to exicute. Please retry")) {
						schedule.setEndDate(newDate);
						try {
							scheduleDAO.updateSchedule(schedule);
						} catch (Exception e) {
							logger.log("Failed to update schedule");
							status = "Something went wrong and request failed to exicute. Please retry";
						}
					}
				}
				else {
					logger.log("The date is not after current end date.");
					status = "The desired schedule end date does not come after the current schedule end date. Please select another valid future end date.";
				}
			}
			else {
				logger.log("Secret code provided is incorrect.");
				status = "Orginizer secret code provided is not correct to complete this action. Please try again with the correct secret code.";
			}
		}
		else {
			logger.log("Schedule does not exist with provided schedule ID.");
			status = "Schedule does not exist with provided schedule ID.";
		}
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
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
			if(!status.equals("Something went wrong and request failed to exicute. Please retry")) {
				//If Monday, time slots are added to a new week, increment the week counter
				if(itterationDate.getDayOfWeek().name() == "MONDAY") {
					currentWeek = currentWeek + 1;
				}

				//check if the current day is Saturday or Sunday. If so skip those days
				if (itterationDate.getDayOfWeek().name() == "SATURDAY" || itterationDate.getDayOfWeek().name() == "SUNDAY") {
					//itterationDate = itterationDate.plusDays(1);
				}
				else {
					//Reset the time slot number counter for day for every new date
					currentSlotNum = 1;

					//Loop through and create time slots for the given time frame
					for (long j = 0; j < numTimeslotsPerDay; j++)
					{
						
						if(!status.equals("Something went wrong and request failed to exicute. Please retry")) {
							//Get the current day of the week in int
							currentDayOfWeek = itterationDate.getDayOfWeek().getValue();

							Timeslot ts = null;
							try {
								ts = tdao.getTimeslotWithTimestemp(scheduleID, LocalDateTime.of(itterationDate, sTime));
							} catch (Exception e1) {
								logger.log("Failed to get timeslot to the RDS's TimeSlot Table");
								status = "Something went wrong and request failed to exicute. Please retry";
							}

							if(ts != null) {
								ts.setWeek(currentWeek);

								try {
									boolean ans = tdao.updateTimeslot(ts);
								} catch (Exception e) {
									logger.log("Failed to update timeslot in the RDS's TimeSlot Table");
									status = "Something went wrong and request failed to exicute. Please retry";
								}
							}
							else {

								//Create new time slot
								ts = new Timeslot(scheduleID, currentWeek, currentDayOfWeek, currentSlotNum, LocalDateTime.of(itterationDate, sTime), false, true);

								//Try to add the time slot to the RDS timeSlot table
								try {
									boolean ans = tdao.addTimeslot(ts);
								} catch (Exception e) {
									logger.log("Failed to add timeslot to the RDS's TimeSlot Table");
									status = "Something went wrong and request failed to exicute. Please retry";
								}
							}
						}

						//Inctiment the new start time for next time slot and the slot number for the day
						sTime = sTime.plusMinutes(duration);
						currentSlotNum = currentSlotNum + 1;
					}
				}
			}
			//Inciment the date by one day and reset the start time for the next day to specified time
			itterationDate = itterationDate.plusDays(1);
			sTime = LocalTime.of(startTime, 0); 
		}
	}

////////////////////////////////////////////////////////////////////////////////////

	LocalDate parseDate(String date) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(date, dtf);
	}

}
