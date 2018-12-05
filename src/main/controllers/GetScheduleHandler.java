package main.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import main.entities.*;

public class GetScheduleHandler implements RequestStreamHandler
{
	public LambdaLogger logger = null;
	
	
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException
	{
		logger = context.getLogger();
		
		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type", "application/json");
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");

	    JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);
		
		GetScheduleResponse response = null;
		
		// extract body from incoming HTTP GET request.
		String body;
		boolean processed = false;
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());
			
			String method = (String) event.get("httpMethod");
			if (method != null && method.equalsIgnoreCase("OPTIONS"))
			{
				logger.log("Options request");
				response = new GetScheduleResponse("name", 200);  // OPTIONS needs a 200 response
		        responseJson.put("body", new Gson().toJson(response));
		        processed = true;
		        body = null;
			} else
			{
				body = (String)event.get("body");
				if (body == null) {
					body = event.toJSONString();  // this is only here to make testing easier
				}
			}
		} catch (ParseException pe)
		{
			logger.log(pe.toString());
			response = new GetScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			CreateScheduleRequest req = new Gson().fromJson(body, CreateScheduleRequest.class);
			logger.log(req.toString());

			/*
			 * From HTML
			 * data["scheduleName"] = arg1;
			 * data["startDate"] = arg2;
			 * data["endDate"] = arg3;
			 * data["startTime"] = arg4;
			 * data["endTime"] = arg5;
			 * data["increment"] = arg6;
			 */

			Schedule newSchedule = new Schedule(req.scheduleName, req.startDate, req.endDate, req.startTime, req.endTime, req.increment);

			// compute proper response
			GetScheduleResponse resp = new GetScheduleResponse(newSchedule);
	        responseJson.put("body", new Gson().toJson(resp));
		}
	}
}
