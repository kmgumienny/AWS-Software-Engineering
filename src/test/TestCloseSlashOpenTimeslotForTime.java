package test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import main.controllers.CloseTimeslotsForTimeHandler;
import main.controllers.OpenTimeslotsForTimeHandler;
import main.database.ScheduleDAO;
import main.database.TimeslotDAO;
import main.entities.Schedule;
import main.entities.Timeslot;

public class TestCloseSlashOpenTimeslotForTime
{	
	// Test Schedule
	static final String SCHEDULE_NAME = "TestOpenSlashCloseTimeslotsForTimeScheduleName";
	static final String SCHEDULE_ID = "TestOpenSlashCloseTimeslotsForDayScheduleID";
	static final String SECRET_CODE = "OldCodeWasTooLong"; //old code was "TestOpenSlashCloseTimeslotsForDayScheduleSecretCode", but it truncated and prevented the creation of the Schedule, lol
	static final String START_DATE = "2018-12-10";
	static final String END_DATE = "2018-12-15";
	static final int SCHEDULE_START_TIME = 10;
	static final int END_TIME = 11;
	static final int INCREMENT = 15;
	
	// Test Timeslot 1
	static final String TIMESLOT_ID_1 = "TestOpenSlashCloseTimeslotsForTimeTimeslotID1";
//		use SCHEDULE_ID for scheduleID
	static final int WEEK = 1;
	static final int DAY_IN_WEEK_1 = 3;
	static final int SLOT_NUM_IN_DAY_1 = 1;
	static final String START_TIME_1 = "10:15";
	
	// Test Timeslot 2
	static final String TIMESLOT_ID_2 = "TestOpenSlashCloseTimeslotsForTimeTimeslotID2";
//		use SCHEDULE_ID for scheduleID
//		use WEEK for week
	static final int DAY_IN_WEEK_2 = 3;
	static final int SLOT_NUM_IN_DAY_2 = 2;
	static final String START_TIME_2 = "10:30";
	
	// Test Timeslot 3
	static final String TIMESLOT_ID_3 = "TestOpenSlashCloseTimeslotsForTimeTimeslotID3";
//		use SCHEDULE_ID for scheduleID
//		use WEEK for week
	static final int DAY_IN_WEEK_3 = 4;
	static final int SLOT_NUM_IN_DAY_3 = 2;
	static final String START_TIME_3 = "10:45";
	
	// Test Timeslot 4
	static final String TIMESLOT_ID_4 = "TestOpenSlashCloseTimeslotsForTimeTimeslotID4";
//		use SCHEDULE_ID for scheduleID
//		use WEEK for week
	static final int DAY_IN_WEEK_4 = 4;
	static final int SLOT_NUM_IN_DAY_4 = 1;
	static final String START_TIME_4 = "10:30";
	
	// Test Timeslot 5
	static final String TIMESLOT_ID_5 = "TestOpenSlashCloseTimeslotsForTimeTimeslotID5";
//		use SCHEDULE_ID for scheduleID
//		use WEEK for week
	static final int DAY_IN_WEEK_5 = 5;
	static final int SLOT_NUM_IN_DAY_5 = 1;
	static final String START_TIME_5 = "10:00";
	
	// Test Timeslot 6
	static final String TIMESLOT_ID_6 = "TestOpenSlashCloseTimeslotsForTimeTimeslotID6";
//		use SCHEDULE_ID for scheduleID
//		use WEEK for week
	static final int DAY_IN_WEEK_6 = 5;
	static final int SLOT_NUM_IN_DAY_6 = 2;
	static final String START_TIME_6 = "10:30";
	
	@Test
	public void testCloseSlashOpenTimeslotForTime() throws IOException
	{
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		TimeslotDAO timeslotDAO = new TimeslotDAO();
		
		// Add schedule to the database
		try
		{
			scheduleDAO.addSchedule(new Schedule(SCHEDULE_NAME, SCHEDULE_ID, SECRET_CODE, LocalDate.parse(START_DATE), LocalDate.parse(END_DATE), SCHEDULE_START_TIME,
					END_TIME, INCREMENT, LocalDateTime.now()));
		}
		catch(Exception e1)
		{
			System.out.print("Failed to add schedule, should mean said schedule already exists, shouldn't effect testing: "
					+ e1.getMessage());
		}
		
		// Add timeslots to the database
		try
		{
			timeslotDAO.addTimeslot(new Timeslot(TIMESLOT_ID_1, SCHEDULE_ID, WEEK, DAY_IN_WEEK_1, SLOT_NUM_IN_DAY_1,
					LocalDateTime.of(2018, Month.DECEMBER, 12, 10, 15, 0), false, true));
			timeslotDAO.addTimeslot(new Timeslot(TIMESLOT_ID_2, SCHEDULE_ID, WEEK, DAY_IN_WEEK_2, SLOT_NUM_IN_DAY_2,
					LocalDateTime.of(2018, Month.DECEMBER, 12, 10, 30, 0), false, true));
			timeslotDAO.addTimeslot(new Timeslot(TIMESLOT_ID_3, SCHEDULE_ID, WEEK, DAY_IN_WEEK_3, SLOT_NUM_IN_DAY_3,
					LocalDateTime.of(2018, Month.DECEMBER, 13, 10, 45, 0), false, true));
			timeslotDAO.addTimeslot(new Timeslot(TIMESLOT_ID_4, SCHEDULE_ID, WEEK, DAY_IN_WEEK_4, SLOT_NUM_IN_DAY_4,
					LocalDateTime.of(2018, Month.DECEMBER, 13, 10, 30, 0), false, true));
			timeslotDAO.addTimeslot(new Timeslot(TIMESLOT_ID_5, SCHEDULE_ID, WEEK, DAY_IN_WEEK_5, SLOT_NUM_IN_DAY_5,
					LocalDateTime.of(2018, Month.DECEMBER, 14, 10, 00, 0), false, true));
			timeslotDAO.addTimeslot(new Timeslot(TIMESLOT_ID_6, SCHEDULE_ID, WEEK, DAY_IN_WEEK_6, SLOT_NUM_IN_DAY_6,
					LocalDateTime.of(2018, Month.DECEMBER, 14, 10, 30, 0), false, true));
		}
		catch (Exception e2)
		{
			System.out.println("Failure to add timeslots.  Should mean they already exist, and should not effect the test: " + e2.getMessage());
		}
		
		CloseTimeslotsForTimeHandler closeHandler = new CloseTimeslotsForTimeHandler();
		OpenTimeslotsForTimeHandler openHandler = new OpenTimeslotsForTimeHandler();
		JsonParser parser = new JsonParser();
		
		// close timeslot
		//create sample Json
		JsonObject input = new JsonObject();
		input.addProperty("scheduleID", SCHEDULE_ID);
		input.addProperty("originizerSecretCode", SECRET_CODE);
		input.addProperty("hour", 10);
		input.addProperty("minute", 30);
		System.out.println(input.toString());
		
		// set the sample json as a ByteArrayInputStream, to be sent into handler.handleRequest(...);
		InputStream closeInputVal = new ByteArrayInputStream(input.toString().getBytes());
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
		// set the sample json as a ByteArrayInputStream, to be sent into handler.handleRequest(...);
		InputStream openInputVal = new ByteArrayInputStream(input.toString().getBytes());
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
