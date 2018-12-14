package main.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import main.database.ScheduleDAO;
import main.entities.Schedule;

public class GetOldSchedulesHandler implements RequestStreamHandler{

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

		GetOldSchedulesResponse response = null;
		
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
				response = new GetOldSchedulesResponse("name", 200);  // OPTIONS needs a 200 response
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
			response = new GetOldSchedulesResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			GetOldSchedulesRequest req = new Gson().fromJson(body, GetOldSchedulesRequest.class);
			logger.log(req.toString());
			status = "OK";

			List<Schedule> schedules = getSchedules();
			List<Schedule> resultList = new ArrayList<Schedule>();
			LocalDateTime timeNow = LocalDateTime.now(ZoneId.of("UTC-5"));
			LocalDateTime compareTime = timeNow.minusHours(req.hoursPassed);


				for(int i = 0; i < schedules.size(); i++) {
					Schedule aSchedule = schedules.get(i);
					if(aSchedule.getCreationDate().isAfter(compareTime) && aSchedule.getCreationDate().isBefore(timeNow)) {
						resultList.add(aSchedule);
					}
					else if(aSchedule.getCreationDate().toLocalDate().equals(compareTime.toLocalDate())) {
						if(aSchedule.getCreationDate().getHour() == compareTime.getHour()) {
							if(aSchedule.getCreationDate().getMinute() == compareTime.getMinute()) {
								resultList.add(aSchedule);
							}
						}
					}
				}
			
			if(status.equals("Something went wrong and request failed to exicute. Please retry")){
				response = new GetOldSchedulesResponse(status, 500);
		        responseJson.put("body", new Gson().toJson(response));
			}
			else if(resultList.size() > 0) {
				// compute proper response for success
				GetOldSchedulesResponse resp = new GetOldSchedulesResponse("Schedules retrieved", resultList);
		        responseJson.put("body", new Gson().toJson(resp));  
			}
			else {
				response = new GetOldSchedulesResponse("No schedules have been created within " + req.hoursPassed + " hours.", 422);
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
	
	List<Schedule> getSchedules() {
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		List<Schedule> schedules = null;

		try {
			schedules = scheduleDAO.getAllSchedules();
			for(int i = 0; i < schedules.size(); i++) { //set all secret codes to null, like in getSchedule
				Schedule aSchedule = schedules.get(i);
				aSchedule.setSecretCode(null);	
			}
		} catch (Exception e) {
			logger.log("Failed to get the schedule.");
			status = "Something went wrong and request failed to exicute. Please retry";
		}

		return schedules;
	}
	
	
////////////////////////////////////////////////////////////////////////////////////
	
	
}
