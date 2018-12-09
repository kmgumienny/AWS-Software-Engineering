package main.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import main.database.MeetingDAO;
import main.database.TimeslotDAO;
import main.entities.Meeting;
import main.entities.Timeslot;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class CreateMeetingHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;
	String status = "OK";

	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
	/*
	 * public Meeting(String meetingID, String scheduleID, String timeslotID, String meetingName, String secretCode)
	{
	 */
	
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to create Meeting");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CreateMeetingResponse response = null;
		
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
				response = new CreateMeetingResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new CreateMeetingResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			CreateMeetingRequest req = new Gson().fromJson(body, CreateMeetingRequest.class);
			logger.log(req.toString());
			status = "OK";
			
			//check if the time slot is already reserved
			TimeslotDAO timeslotDAO = new TimeslotDAO();
			
			//Check if the time slot is reserved and the time slot with the given time slot ID
			Timeslot timeSlot = checkTimeSlot(req.timeslotID, timeslotDAO);
			
			if(timeSlot != null) {
				if(timeSlot.getIsOpen()) {
					if(!timeSlot.getIsReserved()) {
						Meeting newMeeting = createMeeting(req.meetingName, timeslotDAO, timeSlot);

						if(status.equals("Something went wrong and request failed to exicute.")){
							CreateMeetingResponse resp = new CreateMeetingResponse(status, 500);
							responseJson.put("body", new Gson().toJson(resp));
						}
						else {
							CreateMeetingResponse resp = new CreateMeetingResponse("Meeting successifully created and reserved in the given time slot.", newMeeting.getMeetingName(), newMeeting.getMeetingID(), newMeeting.getSecretCode());
							responseJson.put("body", new Gson().toJson(resp));  
						}
					}
					else {
						logger.log("Time slot already reserved");
						CreateMeetingResponse resp = new CreateMeetingResponse("Antoher meeting is already reserved for the selected time slot. Please retry with an unreserved time slot.", 422);
						responseJson.put("body", new Gson().toJson(resp));
					}
				}
				else {
					logger.log("Time slot closed");
					CreateMeetingResponse resp = new CreateMeetingResponse("Selected time slot is currently closed. Please select another open timeslot to reserve a meeting in.", 422);
					responseJson.put("body", new Gson().toJson(resp));
				}
			}
			else {
				CreateMeetingResponse resp = new CreateMeetingResponse("Time slot does not exist with the given time slot ID. Please retry with valid time slot ID.", 422);
				responseJson.put("body", new Gson().toJson(resp));
			}
		}
		
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////
	
	Timeslot checkTimeSlot(String timeslotID, TimeslotDAO timeslotDAO) {
		Timeslot timeSlot = null;
		
		try {
			timeSlot = timeslotDAO.getTimeslot(timeslotID);
		} catch (Exception e1) {
			logger.log("Failed to retrieve time slot");
		}
		
		return timeSlot;
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////
	
	Meeting createMeeting(String meetingName, TimeslotDAO timeslotDAO, Timeslot timeSlot) {
		Meeting meeting = null;
		boolean worked = false;
		
		timeSlot.setIsReserved(true);
		try {
			worked = timeslotDAO.updateTimeslot(timeSlot);
		} catch (Exception e1) {
			logger.log("Time slot failed to update");
			status = "Something went wrong and request failed to exicute.";
		}
		
		if(worked) {
			MeetingDAO meetingDAO = new MeetingDAO();
			meeting = new Meeting(timeSlot.getScheduleID(), timeSlot.getTimeslotID(), meetingName);
			
			try {
				boolean ans = meetingDAO.addMeeting(meeting);
			} catch (Exception e) {
				logger.log("Meeting failed to be created");
				status = "Something went wrong and request failed to exicute.";
			}
		}
		
		return meeting;
	}
}
