package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import main.controllers.CreateScheduleHandler;
import main.controllers.GetScheduleHandler;
import main.database.ScheduleDAO;
import main.entities.Schedule;

public class TestCreateSchedule
{
	// Test Schedule 1
	static final String SCHEDULE_NAME_1 = "TestCreateScheduleTest1";
	static final String SCHEDULE_ID_1 = "CreateScheduleTest1";
	static final String SECRET_CODE_1 = "12345";
	static final String START_DATE_1 = "2018-12-03";
	static final String END_DATE_1 = "2018-12-04";
	static final int START_TIME_1 = 6;
	static final int END_TIME_1 = 11;
	static final int INCREMENT_1 = 10;
	
	// Test Schedule 2
	static final String SCHEDULE_NAME_2 = "TestCreateScheduleTest2";
	static final String SCHEDULE_ID_2 = "CreateScheduleTest2";
	static final String SECRET_CODE_2 = "12345";
	static final String START_DATE_2 = "2018-12-03";
	static final String END_DATE_2 = "2018-12-04";
	static final int START_TIME_2 = 6;
	static final int END_TIME_2 = 11;
	static final int INCREMENT_2 = 10;
	
	@Test
	public void testDAOCreateSchedule()
	{
		ScheduleDAO dao = new ScheduleDAO();
		
		// just in case there are any remnants from past test calls, get rid of the schedule
		try
		{
			dao.deleteSchedule(SCHEDULE_ID_1);
		}
		catch (Exception e)
		{
			fail ("deleteSchedule failed" + e.getMessage());
		}
	    try
	    {
	    	Schedule schedule = new Schedule(SCHEDULE_NAME_1, SCHEDULE_ID_1, SECRET_CODE_1, LocalDate.parse(START_DATE_1), LocalDate.parse(END_DATE_1), START_TIME_1, END_TIME_1, INCREMENT_1, LocalDateTime.now());
	    	boolean b = dao.addSchedule(schedule);
	    	assertTrue(b);
	    }
	    catch (Exception e)
	    {
	    	fail ("create schedule didn't work: " + e.getMessage());
	    }
	    
	    try
	    {
	    	Schedule gotSchedule = dao.getSchedule(SCHEDULE_ID_1);
	    	assertEquals(gotSchedule.getScheduleName(), SCHEDULE_NAME_1);
	    	assertEquals(gotSchedule.getSecretCode(), SECRET_CODE_1);
	    	assertEquals(gotSchedule.getStartDate().toString(), START_DATE_1);
	    	assertEquals(gotSchedule.getEndDate().toString(), END_DATE_1);
	    	assertEquals(gotSchedule.getDayStartTime(), START_TIME_1);
	    	assertEquals(gotSchedule.getDayEndTime(), END_TIME_1);
	    	assertEquals(gotSchedule.getTimeSlotDuration(), INCREMENT_1);
	    }
	    catch (Exception e)
	    {
	    	fail ("get schedule didn't work: " + e.getMessage());
	    }
	    
	}
	
	@Test
	public void testCreateScheduleHandler() throws IOException
	{
		{
			CreateScheduleHandler handler = new CreateScheduleHandler();
			JsonParser parser = new JsonParser();
			
			//create sample Json
			JsonObject input = new JsonObject();
			input.addProperty("scheduleName", SCHEDULE_NAME_2);
			input.addProperty("startDate", START_DATE_2);
			input.addProperty("endDate", END_DATE_2);
			input.addProperty("dailyStartTime", START_TIME_2);
			input.addProperty("dailyEndTime", END_TIME_2);
			input.addProperty("timeSlotDuration", INCREMENT_2);
			
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
			
			// test case
			assertEquals(200, intCode);
		}
	}
}
