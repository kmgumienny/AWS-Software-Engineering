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

import main.database.ScheduleDAO;
import main.database.TimeslotDAO;
import main.entities.Timeslot;

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class OpenSingleTimeslotHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;
	String status = "OK";

	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
	
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to open single time slot");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CloseSingleTimeslotResponse response = null;
		
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
				response = new CloseSingleTimeslotResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new CloseSingleTimeslotResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			CloseSingleTimelsotRequest req = new Gson().fromJson(body, CloseSingleTimelsotRequest.class);
			logger.log(req.toString());
			status = "OK";
			
			openSingleTimeSlot(req.timeSlotID, req.originizerSecretCode);
			
			//Response creation
			if(status.equals("OK")){
				response = new CloseSingleTimeslotResponse("Selected time slot opened successifully.");
		        responseJson.put("body", new Gson().toJson(response));
			}
			else if(status.equals("Something went wrong and request failed to exicute. Please retry")) {
				
				response = new CloseSingleTimeslotResponse(status, 500);
		        responseJson.put("body", new Gson().toJson(response));
			}
			else {
				response = new CloseSingleTimeslotResponse(status, 422);
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
	
	void openSingleTimeSlot(String timeslotID, String originizerSecretCode) {
		TimeslotDAO timeSlotDAO = new TimeslotDAO();
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		String secretCode = null;
		Timeslot timeSlot = null;

		try {
			timeSlot = timeSlotDAO.getTimeslot(timeslotID);
			secretCode = scheduleDAO.getSchedule(timeSlot.getScheduleID()).getSecretCode();
		} catch (Exception e) {
			logger.log("Failed to get timeslot or the schedule.");
			status = "Something went wrong and request failed to exicute. Please retry";
		}

		if(timeSlot != null) {
			if(secretCode.equals(originizerSecretCode)) {
				if(!timeSlot.getIsOpen()) {
					timeSlot.setIsOpen(true);
					try {
						timeSlotDAO.updateTimeslot(timeSlot);
					} catch (Exception e) {
						logger.log("Failed to update timeslot.");
						status = "Something went wrong and request failed to exicute. Please retry";
					}
				}
				else {
					logger.log("Time slot is already opened.");
					status = "Time slot is already opened.";
				}
			}
			else {
				logger.log("Secret code provided is incorrect.");
				status = "Orginizer secret code provided is not correct to complete this action. Please try again with the correct secret code.";
			}
		}
		else {
			logger.log("Incorrect time slot ID or time slot does not exist.");
			status = "Incorrect time slot ID or time slot does not exist.";
		}
	}
	
}
