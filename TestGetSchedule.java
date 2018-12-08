package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import main.controllers.GetScheduleHandler;
import main.database.ScheduleDAO;
import main.entities.Schedule;

public class TestGetSchedule
{
	static final String OTHER_SCHEDULE_NAME = "Test Schedule Two";
	static final String SCHEDULE_NAME = "Test Schedule";
	static final String SCHEDULE_ID = "TestId12345";
	static final String SECRET_CODE = "TestCode321";
	static final String START_DATE = "2018-12-05";
	static final String END_DATE = "2018-12-14";
	static final int DAY_START_TIME = 10;
	static final int DAY_END_TIME = 14;
	static final int TIME_SLOT_DURATION = 10;
	
	@Test
	public void testDAOGetSchedule()
	{
		ScheduleDAO dao = new ScheduleDAO();
		Schedule schedule = null;
		createSchedule();
	    try
	    {
	    	schedule = dao.getSchedule(SCHEDULE_ID);
	    } catch (Exception e)
	    {
	    	fail ("didn't work:" + e.getMessage());
	    }
	    
	    assertEquals(schedule.getScheduleID(), SCHEDULE_ID);
	    assertEquals(schedule.getScheduleName(), SCHEDULE_NAME);
	    assertEquals(schedule.getSecretCode(), SECRET_CODE);
	    assertEquals(schedule.getDayStartTime(), DAY_START_TIME);
	    assertEquals(schedule.getDayEndTime(), DAY_END_TIME);
	    assertEquals(schedule.getTimeSlotDuration(), TIME_SLOT_DURATION);
	}
	
	@Test
	public void testGetScheduleHandler() throws IOException
	{
		GetScheduleHandler handler = new GetScheduleHandler();
		JsonParser parser = new JsonParser();
		
		//create sample Json
		JsonObject input = new JsonObject();
		input.addProperty("scheduleName", OTHER_SCHEDULE_NAME);
		input.addProperty("startDate", START_DATE);
		input.addProperty("endDate", END_DATE);
		input.addProperty("dailyStartTime", DAY_START_TIME);
		input.addProperty("dailyEndTime", DAY_END_TIME);
		input.addProperty("timeSlotDuration", TIME_SLOT_DURATION);
		
		// set the sample json as a ByteArrayInputStream, to be sent into handler.handleRequest(...);
		InputStream inputVal = new ByteArrayInputStream(input.toString().getBytes());
		OutputStream output = new ByteArrayOutputStream();
		Context context = new TestContext();

		// request is handled
		handler.handleRequest(inputVal, output, context);
		
		// convert output from type ByteArrayInputStream to String, and then parse it into a Json
		JsonObject object = parser.parse(output.toString()).getAsJsonObject();
		
		// get "body" String from the output Json (because that is how we have it set up, apparently)
		//	as type JsonPrimitive, and then convert it to type String in order to convert it again to type JsonObject
		JsonObject bodyJson = parser.parse(object.getAsJsonPrimitive("body").getAsString()).getAsJsonObject();

		// extract the httpCode int from the previously parsed bodyJson
		int intCode = bodyJson.getAsJsonPrimitive("httpCode").getAsInt();
		//System.out.println("intCode: " + intCode);
		
		// test case
		assertEquals(200, intCode);
		
		
		
	}
	
	public void createSchedule()
	{
		ScheduleDAO dao = new ScheduleDAO();
		Schedule schedule = new Schedule(SCHEDULE_NAME, SCHEDULE_ID,
				SECRET_CODE, START_DATE, END_DATE,
				DAY_START_TIME, DAY_END_TIME, TIME_SLOT_DURATION);
		try
		{
		dao.addSchedule(schedule);
		} catch (Exception e)
		{
			fail ("did not add schedule " + e.getMessage());
		}
	}
}
