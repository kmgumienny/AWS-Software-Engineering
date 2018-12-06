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
		
		JsonObject input = new JsonObject();
		input.addProperty("scheduleName", OTHER_SCHEDULE_NAME);
		input.addProperty("startDate", START_DATE);
		input.addProperty("endDate", END_DATE);
		input.addProperty("dailyStartTime", DAY_START_TIME);
		input.addProperty("dailyEndTime", DAY_END_TIME);
		input.addProperty("timeSlotDuration", TIME_SLOT_DURATION);
		
		JsonObject body = new JsonObject();
		body.addProperty("body", input.toString());
		
		String jsonRequest = body.toString();
		
		InputStream inputVal = new ByteArrayInputStream(jsonRequest.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		
		
		handler.handleRequest(inputVal, output, createContext("a")); 
		
		String status = new JsonParser().parse(output.toString()).getAsJsonObject().get("httpCode").getAsString();
		
		assertEquals("200", status);
		
		
		
	}
	
	Context createContext(String apiCall) {
      TestContext ctx = new TestContext();
      ctx.setFunctionName(apiCall);
      return ctx;
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
