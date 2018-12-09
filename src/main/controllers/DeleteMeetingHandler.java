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
public class DeleteMeetingHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;

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
		logger.log("Loading Java Lambda handler to delete meeting");

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
			DeleteMeetingRequest req = new Gson().fromJson(body, DeleteMeetingRequest.class);
			logger.log(req.toString());

			String ans = deleteMeeting(req.meetingID, req.secretCode);
			
			// compute proper response:
			if(ans.equals("Meeting is deleted successifully.")) {
				DeleteMeetingResponse resp = new DeleteMeetingResponse(ans);
		        responseJson.put("body", new Gson().toJson(resp));  
			}
			else if(ans.equals("Meeting does not exist with the given meeting ID.")) {
				DeleteMeetingResponse resp = new DeleteMeetingResponse(ans, 422);
		        responseJson.put("body", new Gson().toJson(resp));  
			}
			else if(ans.equals("Secret code provided was not correct.")) {
				DeleteMeetingResponse resp = new DeleteMeetingResponse(ans, 422);
		        responseJson.put("body", new Gson().toJson(resp));  
			}
			else if(ans.equals("Something went wrong and request failed to exicute." )) {
				DeleteMeetingResponse resp = new DeleteMeetingResponse(ans, 500);
		        responseJson.put("body", new Gson().toJson(resp));  
			}
		}
		
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
	}
	
	
////////////////////////////////////////////////////////////////////////////////////
	
	String deleteMeeting(String meetingID, String secretCode) {
		MeetingDAO meetingDAO = new MeetingDAO();
		Meeting meeting = null;
		String response = "Something went wrong and request failed to exicute.";
		
		//Get the meeting with specified meeting ID
		try {
			meeting = meetingDAO.getMeeting(meetingID);
		} catch (Exception e) {
			logger.log("Failed to get meeting.");
			response = "Meeting does not exist with the given meeting ID.";
		}
		
		if(meeting != null) {
			//check of the secret code is correct
			if(meeting.getSecretCode().equals(secretCode)) {
				TimeslotDAO timeSlotDAO = new TimeslotDAO();
				Timeslot timeSlot = null;
				
				//Get the time slot associated with the meeting being deleted
				try {
					timeSlot = timeSlotDAO.getTimeslot(meeting.getTimeslotID());
				} catch (Exception e) {
					response = "Something went wrong and request failed to exicute.";
				}
				
				if(timeSlot != null) {
					timeSlot.setIsReserved(false);
					boolean timeSlotUpdated = false;
					
					//Update the time slot no not have meeting resvered
					try {
						timeSlotDAO.updateTimeslot(timeSlot);
						timeSlotUpdated = true;
					} catch (Exception e) {
						logger.log("Failed to update time slot");
						response = "Something went wrong and request failed to exicute.";
					}
					
					if(timeSlotUpdated) {
						//Delete the meeting
						try {
							boolean ans = meetingDAO.deleteMeeting(meetingID);
							response =  "Meeting is deleted successifully.";
						} catch (Exception e) {
							logger.log("Failed to delete meeting");
							response = "Something went wrong and request failed to exicute.";
						}
					}
				}
			}
			else {
				logger.log("Secret code provided was not correct.");
				response = "Secret code provided was not correct.";
			}
		}
		else {
			logger.log("Failed to get meeting.");
			response = "Meeting does not exist with the given meeting ID.";
		}
		
		return response;
	}
}
