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

			/*
			 * From HTML:
			 * data["scheduleID"] = arg1;
			 * data["meetingName"] = arg2;
			 */
			
			Meeting newMeeting = createMeeting(req.timeslotID, req.meetingName);
			String meetingName = newMeeting.getMeetingName();
			String meetingID = newMeeting.getMeetingID();
			String secretCode = newMeeting.getSecretCode();

			// compute proper response
			CreateMeetingResponse resp = new CreateMeetingResponse("OK", meetingName, meetingID, secretCode);
	        responseJson.put("body", new Gson().toJson(resp));  
		}
		
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
	}
	
	
///////////////////////////////// Milap Code edition Working 
	
	Meeting createMeeting(String timeslotID, String meetingName) {
		if (logger != null) { logger.log("in createMeeting"); }
		TimeslotDAO timeslotDAO = new TimeslotDAO();
		MeetingDAO meetingDAO = new MeetingDAO();
		
		Timeslot ts = null;
		try {
			ts = timeslotDAO.getTimeslot(timeslotID);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Meeting meeting = null;
		if(ts != null) {
			if(ts.getIsReserved() == false) {
				meeting = new Meeting(ts.getScheduleID(), ts.getTimeslotID(), meetingName);
				ts.setIsReserved(true);
				try {
					boolean worked = timeslotDAO.updateTimeslot(ts);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					boolean ans = meetingDAO.addMeeting(meeting);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return meeting;
	}
}
