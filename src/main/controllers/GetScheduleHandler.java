package main.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import main.database.MeetingDAO;
import main.database.ScheduleDAO;
import main.database.TimeslotDAO;
import main.entities.Meeting;
import main.entities.Schedule;
import main.entities.Timeslot;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class GetScheduleHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;
	String status = "OK";

	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
	
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to get timeslots and meeting for the given schedule ID");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		GetScheduleResponse response = null;
		
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
				response = new GetScheduleResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new GetScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			GetScheduleRequest req = new Gson().fromJson(body, GetScheduleRequest.class);
			logger.log(req.toString());

			Schedule schedule = getSchedule(req.scheduleID);
			
			if(status.equals("Something went wrong and request failed to exicute. Please retry")){
				response = new GetScheduleResponse(status, 500);
		        responseJson.put("body", new Gson().toJson(response));
			}
			else if(schedule != null) {
				List<Timeslot> timeSlots = getScheduleTimeslots(req.scheduleID);
				List<Meeting>  meetings = getScheduleMeetings(req.scheduleID);
				
				// compute proper response for success
				GetScheduleResponse resp = new GetScheduleResponse("Schedule, timeslots and meetings retrieved.", schedule, timeSlots, meetings);
		        responseJson.put("body", new Gson().toJson(resp));  
			}
			else {
				response = new GetScheduleResponse("Schedule does not exist with given schedule ID of: " + req.scheduleID + ".", 422);
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
	
	Schedule getSchedule(String scheduleID) {
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		Schedule schedule = null;

		try {
			schedule =scheduleDAO.getSchedule(scheduleID);
		} catch (Exception e) {
			logger.log("Failed to get the schedule.");
			status = "Something went wrong and request failed to exicute. Please retry";
		}

		return schedule;
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
	List<Timeslot> getScheduleTimeslots(String scheduleID) {
		TimeslotDAO timeSlotDAO = new TimeslotDAO();
		List<Timeslot> timeSlots = null;
		
		try {
			timeSlots = timeSlotDAO.getAllTimeslotsWithScheduleID(scheduleID);
		} catch (Exception e) {
			logger.log("Failed to get timeslots.");
			status = "Something went wrong and request failed to exicute. Please retry";
		}
		
		return timeSlots;
	}
	
////////////////////////////////////////////////////////////////////////////////////
	
	List<Meeting> getScheduleMeetings(String scheduleID) {
		MeetingDAO meetingDAO = new MeetingDAO();
		List<Meeting> meetings = null;
		
		try {
			meetings = meetingDAO.getAllMeetingsWithScheduleID(scheduleID);
		} catch (Exception e) {
			logger.log("Failed to get meetings.");
			status = "Something went wrong and request failed to exicute. Please retry";
		}
		
		return meetings;
	}
	
}
