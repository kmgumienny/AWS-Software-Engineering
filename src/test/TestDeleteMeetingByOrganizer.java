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

import main.controllers.DeleteMeetingByOrginizerHandler;
import main.database.MeetingDAO;
import main.database.ScheduleDAO;
import main.database.TimeslotDAO;
import main.entities.Meeting;
import main.entities.Schedule;
import main.entities.Timeslot;

public class TestDeleteMeetingByOrganizer
{
	// Test Meeting
	static final String MEETING_ID = "TestDeleteMeetingByOrganizerID";
	static final String SCHEDULE_ID = "TestDeleteMeetingByOrganizerScheduleID";
	static final String TIMESLOT_ID = "TestDeleteMeetingByOrganizerTimeslotID";
	static final String MEETING_NAME = "TestDeleteMeetingByOrganizerName";
	static final String SECRET_CODE = "OldCodeTooLong";
	
	// Test Timeslot
//	use TIMESLOT_ID_2 for timeslotID
//	use SCHEDULE_ID_2 for scheduleID
	static final int WEEK = 2;
	static final int DAY_IN_WEEK = 2;
	static final int SLOT_NUM_IN_DAY = 2;
	
	// Test Schedule
	static final String SCHEDULE_NAME = "TestDeleteMeetingByOrganizerScheduleName";
//	use SCHEDULE_ID for scheduleID
//	use SECRET_CODE for secretCode
	static final String START_DATE = "2018-12-10";
	static final String END_DATE = "2018-12-12";
	static final int DAY_START_TIME = 10;
	static final int DAY_END_TIME = 11;
	static final int DURATION = 15;
	
	@Test
	public void testDeleteMeetingByOrganizerHandler() throws IOException
	{
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		TimeslotDAO timeslotDAO = new TimeslotDAO();
		Timeslot timeslot = new Timeslot(TIMESLOT_ID, SCHEDULE_ID, WEEK, DAY_IN_WEEK, SLOT_NUM_IN_DAY,
				LocalDateTime.of(2018, Month.DECEMBER, 10, 10, 30, 0), false, true);
		MeetingDAO meetingDAO = new MeetingDAO();
		Meeting meeting = new Meeting(MEETING_ID, SCHEDULE_ID, TIMESLOT_ID, MEETING_NAME, SECRET_CODE);
		
		// Add the Schedule to the database
		try
		{
			scheduleDAO.addSchedule(new Schedule(SCHEDULE_NAME, SCHEDULE_ID, SECRET_CODE, LocalDate.parse(START_DATE), LocalDate.parse(END_DATE),
					DAY_START_TIME, DAY_END_TIME, DURATION, LocalDateTime.now()));
		}
		catch(Exception e)
		{
			System.out.println("adding schedule failed, should mean it already exists, shouldn't effect testing: " + e.getMessage());
		}
		
		// Add the Timeslot to which the meeting will belong
		try
		{
			timeslotDAO.addTimeslot(timeslot);
		}
		catch(Exception e)
		{
			System.out.println("adding timeslot failed, should mean it already exists, shouldn't effect testing: " + e.getMessage());
		}
		
		// Add the meeting that will be deleted
		try
		{
			meetingDAO.addMeeting(meeting);
		}
		catch(Exception e)
		{
			System.out.println("This meeting already exists.  This should not negatively effect the test: " + e.getMessage());
		}
		
		DeleteMeetingByOrginizerHandler handler = new DeleteMeetingByOrginizerHandler();
		// TODO: Fix the damn spelling, and make sure it doesn't break everything
		JsonParser parser = new JsonParser();
		
		//create sample Json
		JsonObject input = new JsonObject();
		input.addProperty("meetingID", MEETING_ID);
		input.addProperty("originizerSecretCode", SECRET_CODE);
		
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
}
