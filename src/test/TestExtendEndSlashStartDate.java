package test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import main.controllers.ExtandEndDateHandler;
import main.controllers.ExtandStartDateHandler;
import main.database.ScheduleDAO;
import main.entities.Schedule;

public class TestExtendEndSlashStartDate
{

	
//	this.scheduleID = scheduleID;
//	this.originizerSecretCode = originizerSecretCode;
//	this.newDate = newDate;
	
	// Test Schedule
	static final String SCHEDULE_NAME = "TestExtendEnd/StartDateScheduleName";
	static final String SCHEDULE_ID = "TestExtendEnd/StartDateScheduleID";
	static final String SECRET_CODE = "TestExtendEnd/StartDateSecretCode";
	static final String START_DATE = "2018-12-10";
	static final String END_DATE = "2018-12-12";
	static final int DAY_START_TIME = 10;
	static final int DAY_END_TIME = 11;
	static final int DURATION = 15;
	
	static final String NEW_START_DATE = "2018-12-09";
	static final String NEW_END_DATE = "2018-12-14";
	
	
	@Test
	public void testExtendEndSlashStartDate() throws IOException
	{
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		
		// Confirm this Schedule does not exist in the database
		try
		{
			scheduleDAO.deleteSchedule(SCHEDULE_ID);
		}
		catch (Exception e1)
		{
			System.out.println("Delete Schedule failed.  This means it does not exist, and will not effect the testing: " + e1.getMessage());
		}
		
		// Add the Schedule to the database
		try
		{
			scheduleDAO.addSchedule(new Schedule(SCHEDULE_NAME, SCHEDULE_ID, SECRET_CODE, START_DATE, END_DATE,
					DAY_START_TIME, DAY_END_TIME, DURATION));
		}
		catch(Exception e)
		{
			System.out.println("adding schedule failed, should mean it already exists, shouldn't effect testing: " + e.getMessage());
		}
		
		ExtandEndDateHandler endHandler = new ExtandEndDateHandler();
		ExtandStartDateHandler startHandler = new ExtandStartDateHandler();
		// TODO: Fix the damn spelling, and make sure it doesn't break everything
		JsonParser parser = new JsonParser();
		
		//create sample Json
		JsonObject endInput = new JsonObject();
		endInput.addProperty("scheduleID", SCHEDULE_ID);
		endInput.addProperty("organizerSecretCode", SECRET_CODE);
		endInput.addProperty("newDate", NEW_END_DATE);
		
		// set the sample json as a ByteArrayInputStream, to be sent into handler.handleRequest(...);
		InputStream endInputVal = new ByteArrayInputStream(endInput.toString().getBytes());
		OutputStream endOutput = new ByteArrayOutputStream();
		Context endContext = new TestContext();

		// request is handled
		endHandler.handleRequest(endInputVal, endOutput, endContext);
		
		// convert output from type ByteArrayInputStream to String, and then parse it into a Json
		JsonObject endObject = parser.parse(endOutput.toString()).getAsJsonObject();
		
		// get "body" String from the output Json (because that is how we have it set up, apparently)
		//	as type JsonPrimitive, and then convert it to type String in order to convert it again to type JsonObject
		JsonObject endBodyJson = parser.parse(endObject.getAsJsonPrimitive("body").getAsString()).getAsJsonObject();

		// extract the httpCode int from the previously parsed bodyJson
		int endIntCode = endBodyJson.getAsJsonPrimitive("httpCode").getAsInt();
		//System.out.println("intCode: " + intCode);
		
		// test case
		assertEquals(200, endIntCode);
		
		//create sample Json
		JsonObject startInput = new JsonObject();
		startInput.addProperty("scheduleID", SCHEDULE_ID);
		startInput.addProperty("organizerSecretCode", SECRET_CODE);
		startInput.addProperty("newStartDate", NEW_START_DATE);
		
		// set the sample json as a ByteArrayInputStream, to be sent into handler.handleRequest(...);
		InputStream startInputVal = new ByteArrayInputStream(startInput.toString().getBytes());
		OutputStream startOutput = new ByteArrayOutputStream();
		Context startContext = new TestContext();

		// request is handled
		startHandler.handleRequest(startInputVal, startOutput, startContext);
		
		// convert output from type ByteArrayInputStream to String, and then parse it into a Json
		JsonObject startObject = parser.parse(startOutput.toString()).getAsJsonObject();
		
		// get "body" String from the output Json (because that is how we have it set up, apparently)
		//	as type JsonPrimitive, and then convert it to type String in order to convert it again to type JsonObject
		JsonObject startBodyJson = parser.parse(startObject.getAsJsonPrimitive("body").getAsString()).getAsJsonObject();

		// extract the httpCode int from the previously parsed bodyJson
		int startIntCode = startBodyJson.getAsJsonPrimitive("httpCode").getAsInt();
		//System.out.println("intCode: " + intCode);
		
		// test case
		assertEquals(200, startIntCode);
	}
	
}
