package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import main.controllers.CloseSingleTimeslotHandler;
import main.controllers.CreateMeetingHandler;
import main.controllers.OpenSingleTimeslotHandler;
import main.database.ScheduleDAO;
import main.database.TimeslotDAO;
import main.entities.Schedule;
import main.entities.Timeslot;

public class TestOpenSlashCloseTimeslot
{
	// Test Schedule
	static final String SCHEDULE_NAME = "TestOpenSlashCloseTimeslotScheduleName";
	static final String SCHEDULE_ID = "TestOpenSlashCloseTImeslotScheduleID";
	static final String SECRET_CODE = "TestOpenSlashCloseTimeslotScheduleSecretCode";
	static final String START_DATE = "2018-12-03";
	static final String END_DATE = "2018-12-04";
	static final int SCHEDULE_START_TIME = 6;
	static final int END_TIME = 11;
	static final int INCREMENT = 10;
	
	//public Schedule(String scheduleName, String scheduleID, String secretCode, String startDate, String endDate, int dayStartTime, int dayEndTime, int timeSlotDuration){
	
	
	// Test Timeslot
	static final String TIMESLOT_ID = "TestOpenSlashCloseTimeslotID1";
//	use SCHEDULE_ID for scheduleID
	static final int WEEK = 1;
	static final int DAY_IN_WEEK = 3;
	static final int SLOT_NUM_IN_DAY = 2;
		
	@Test
	public void testCloseSingleTimeSlotHandler() throws IOException
	{
		LocalDateTime TIMESLOT_START_TIME = LocalDateTime.of(2018, Month.DECEMBER, 5, 12, 30, 0);
		
		TimeslotDAO dao = new TimeslotDAO();
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		
		Timeslot timeslot = new Timeslot(TIMESLOT_ID, SCHEDULE_ID, WEEK, DAY_IN_WEEK, SLOT_NUM_IN_DAY, TIMESLOT_START_TIME, false, true);
		Schedule schedule = new Schedule(SCHEDULE_NAME, SCHEDULE_ID, SECRET_CODE, START_DATE, END_DATE, 
				SCHEDULE_START_TIME, END_TIME, INCREMENT);

		// Add the schedule to the database
		try
		{
			scheduleDAO.addSchedule(schedule);
		}
		catch (Exception e)
		{
			fail ("addSchedule failed: " + e.getMessage());
		}

		// Add the timeslot to the database
		try
		{
			dao.addTimeslot(timeslot);
		}
		catch (Exception e)
		{
			System.out.println("A timeslot with ID identical to that which is trying to be added"
					+ " already exists.  This will not negatively impact the test.");
		}
		
		CloseSingleTimeslotHandler closeHandler = new CloseSingleTimeslotHandler();
		OpenSingleTimeslotHandler openHandler = new OpenSingleTimeslotHandler();
		JsonParser parser = new JsonParser();
		
		// close timeslot
		//create sample Json
		JsonObject closeInput = new JsonObject();
		closeInput.addProperty("timeSlotID", TIMESLOT_ID);
		closeInput.addProperty("organizerSecretCode", SECRET_CODE);
		System.out.println(closeInput.toString());
		
		// set the sample json as a ByteArrayInputStream, to be sent into handler.handleRequest(...);
		InputStream closeInputVal = new ByteArrayInputStream(closeInput.toString().getBytes());
		OutputStream closeOutput = new ByteArrayOutputStream();
		Context closeContext = new TestContext();

		// request is handled
		closeHandler.handleRequest(closeInputVal, closeOutput, closeContext);
		
		// convert output from type ByteArrayInputStream to String, and then parse it into a Json
		JsonObject closeObject = parser.parse(closeOutput.toString()).getAsJsonObject();
		
		// get "body" String from the output Json (because that is how we have it set up, apparently)
		//	as type JsonPrimitive, and then convert it to type String in order to convert it again to type JsonObject
		JsonObject closeBodyJson = parser.parse(closeObject.getAsJsonPrimitive("body").getAsString()).getAsJsonObject();

		// extract the httpCode int from the previously parsed bodyJson
		int closeIntCode = closeBodyJson.getAsJsonPrimitive("httpCode").getAsInt();
		
		// test case
		assertEquals(200, closeIntCode);
		
		// open timeslot
		//create sample Json
		JsonObject openInput = new JsonObject();
		openInput.addProperty("timeSlotID", TIMESLOT_ID);
		openInput.addProperty("organizerSecretCode", SECRET_CODE);
		System.out.println(openInput.toString());
		
		// set the sample json as a ByteArrayInputStream, to be sent into handler.handleRequest(...);
		InputStream openInputVal = new ByteArrayInputStream(openInput.toString().getBytes());
		OutputStream openOutput = new ByteArrayOutputStream();
		Context openContext = new TestContext();

		// request is handled
		openHandler.handleRequest(openInputVal, openOutput, openContext);
		
		// convert output from type ByteArrayInputStream to String, and then parse it into a Json
		JsonObject openObject = parser.parse(openOutput.toString()).getAsJsonObject();
		
		// get "body" String from the output Json (because that is how we have it set up, apparently)
		//	as type JsonPrimitive, and then convert it to type String in order to convert it again to type JsonObject
		JsonObject openBodyJson = parser.parse(openObject.getAsJsonPrimitive("body").getAsString()).getAsJsonObject();

		// extract the httpCode int from the previously parsed bodyJson
		int openIntCode = openBodyJson.getAsJsonPrimitive("httpCode").getAsInt();
		
		// test case
		assertEquals(200, openIntCode);
	}
}
