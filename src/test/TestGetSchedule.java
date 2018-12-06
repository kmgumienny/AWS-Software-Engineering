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
		
		//create sample Json
		JsonObject input = new JsonObject();
		input.addProperty("scheduleName", OTHER_SCHEDULE_NAME);
		input.addProperty("startDate", START_DATE);
		input.addProperty("endDate", END_DATE);
		input.addProperty("dailyStartTime", DAY_START_TIME);
		input.addProperty("dailyEndTime", DAY_END_TIME);
		input.addProperty("timeSlotDuration", TIME_SLOT_DURATION);
		System.out.println("input: " + input);
		
		// set the sample json as a ByteArrayInputStream, to be sent into handler.handleRequest(...);
		InputStream inputVal = new ByteArrayInputStream(input.toString().getBytes());
		OutputStream output = new ByteArrayOutputStream();
		Context context = new TestContext();
		
		// request is handled
		handler.handleRequest(inputVal, output, context);
		
		System.out.println("1: " + output);
		
		
		// take the output ByteArrayOutputStream, convert it into a string, so it can then be turned into a Json
		String stringOutput = output.toString();
		
	
		// parse the stringOutput (converted from the output as type ByteArrayOutputStream), and make it a JsonObject
		JsonParser parserOutput = new JsonParser();
		JsonElement element = parserOutput.parse(stringOutput);
		JsonObject object = element.getAsJsonObject();
		
		System.out.println("getAsJsonObject: " + object.getClass() + object);
		
		// from here, I tried the following line of code, but it made no difference in the error thrown, and it is not
		//	allowing me to get the data under "body" in the output json
		//object = object.getAsJsonObject();
		JsonArray array = object.getAsJsonArray("body");
		//JsonElement code = object.get("httpCode");
		//JsonArray array = object.getAsJsonArray("body");
		
		
		System.out.println("2: " + stringOutput);
		System.out.println("3; " + parserOutput);
		System.out.println("4: " + element);
		System.out.println("5: " + object);
		System.out.println("6: " + array);
		
		
		//System.out.println("6: " + element2);
		//System.out.print("7: " + status);
		//String status = new JsonParser().parse(output.toString()).getAsJsonObject().get("httpCode").getAsString();
		
		//assertEquals("200", status);
		
		
		
	}
	
//	Context createContext(String apiCall) {
//      TestContext ctx = new TestContext();
//      ctx.setFunctionName(apiCall);
//      return ctx;
//  }
	
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
