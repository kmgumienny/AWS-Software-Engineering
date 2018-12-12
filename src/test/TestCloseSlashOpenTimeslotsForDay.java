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

import main.controllers.CloseTimeslotsForDayHandler;
import main.controllers.OpenTimeslotsForDayHandler;
import main.database.ScheduleDAO;
import main.database.TimeslotDAO;
import main.entities.Schedule;
import main.entities.Timeslot;

public class TestCloseSlashOpenTimeslotsForDay
{
	// Test Schedule
	static final String SCHEDULE_NAME = "TestOpenSlashCloseTimeslotsForDayScheduleName";
	static final String SCHEDULE_ID = "TestOpenSlashCloseTimeslotsForDayScheduleID";
	static final String SECRET_CODE = "OldCodeWasTooLong"; //old code was "TestOpenSlashCloseTimeslotsForDayScheduleSecretCode", but it truncated and prevented the creation of the Schedule, lol
	static final String START_DATE = "2018-12-10";
	static final String END_DATE = "2018-12-14";
	static final int SCHEDULE_START_TIME = 10;
	static final int END_TIME = 11;
	static final int INCREMENT = 15;
	
	// Test Timeslot 1
	static final String TIMESLOT_ID_1 = "TestOpenSlashCloseTimeslotsForDayTimeslotID1";
//	use SCHEDULE_ID for scheduleID
	static final int WEEK = 1;
	static final int DAY_IN_WEEK = 3;
	static final int SLOT_NUM_IN_DAY_1 = 1;
	static final String START_TIME_1 = "10:00";
	
	// Test Timeslot 2
	static final String TIMESLOT_ID_2 = "TestOpenSlashCloseTimeslotsForDayTimeslotID2";
//	use SCHEDULE_ID for scheduleID
//	use WEEK for week
//	use DAY_IN_WEEK for dayInWeek
	static final int SLOT_NUM_IN_DAY_2 = 2;
	static final String START_TIME_2 = "10:15";
	
	// Test Timeslot 3
	static final String TIMESLOT_ID_3 = "TestOpenSlashCloseTimeslotsForDayTimeslotID3";
//	use SCHEDULE_ID for scheduleID
//	use WEEK for week
//	use DAY_IN_WEEK for dayInWeek
	static final int SLOT_NUM_IN_DAY_3 = 3;
	static final String START_TIME_3 = "10:30";
	
	// Test Timeslot 4
	static final String TIMESLOT_ID_4 = "TestOpenSlashCloseTimeslotsForDayTimeslotID4";
//	use SCHEDULE_ID for scheduleID
//	use WEEK for week
//	use DAY_IN_WEEK for dayInWeek
	static final int SLOT_NUM_IN_DAY_4 = 4;
	static final String START_TIME_4 = "10:45";
	
	@Test
	public void testCloseTimeslotsForDay() throws IOException
	{
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		TimeslotDAO timeslotDAO = new TimeslotDAO();
		
		// Adds the schedule to the database
		try
		{
			scheduleDAO.addSchedule(new Schedule(SCHEDULE_NAME, SCHEDULE_ID, SECRET_CODE, LocalDate.parse(START_DATE), LocalDate.parse(END_DATE), SCHEDULE_START_TIME,
					END_TIME, INCREMENT, LocalDateTime.now()));
		}
		catch(Exception e1)
		{
			System.out.println("This means the test schedule already exists, should not effect the test: " + e1.getMessage());
		}
		
		// Add timeslots to the database
		try
		{
			timeslotDAO.addTimeslot(new Timeslot(TIMESLOT_ID_1, SCHEDULE_ID, WEEK, DAY_IN_WEEK, SLOT_NUM_IN_DAY_1,
					LocalDateTime.of(2018, Month.DECEMBER, 10, 10, 0, 0), false, true));
			timeslotDAO.addTimeslot(new Timeslot(TIMESLOT_ID_2, SCHEDULE_ID, WEEK, DAY_IN_WEEK, SLOT_NUM_IN_DAY_2,
					LocalDateTime.of(2018, Month.DECEMBER, 10, 10, 15, 0), false, true));
			timeslotDAO.addTimeslot(new Timeslot(TIMESLOT_ID_3, SCHEDULE_ID, WEEK, DAY_IN_WEEK, SLOT_NUM_IN_DAY_3,
					LocalDateTime.of(2018, Month.DECEMBER, 10, 10, 30, 0), false, true));
			timeslotDAO.addTimeslot(new Timeslot(TIMESLOT_ID_4, SCHEDULE_ID, WEEK, DAY_IN_WEEK, SLOT_NUM_IN_DAY_4,
					LocalDateTime.of(2018, Month.DECEMBER, 10, 10, 45, 0), false, true));
		}
		catch (Exception e2)
		{
			System.out.println("Failure to add timeslots.  SHould mean they already exist, and should not effect the test: " + e2.getMessage());
		}
		
		CloseTimeslotsForDayHandler closeHandler = new CloseTimeslotsForDayHandler();
		OpenTimeslotsForDayHandler openHandler = new OpenTimeslotsForDayHandler();
		JsonParser parser = new JsonParser();
		
		// close timeslot
		//create sample Json
		JsonObject input = new JsonObject();
		input.addProperty("scheduleID", SCHEDULE_ID);
		input.addProperty("originizerSecretCode", SECRET_CODE);
		input.addProperty("closeDate", START_DATE);
		input.addProperty("openDate", START_DATE);
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
