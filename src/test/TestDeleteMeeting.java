package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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

import main.controllers.DeleteMeetingHandler;
import main.database.MeetingDAO;
import main.database.TimeslotDAO;
import main.entities.Meeting;
import main.entities.Timeslot;

public class TestDeleteMeeting{
	
	// Test Meeting 1
	static final String MEETING_ID_1 = "TestDeleteMeetingID1";
	static final String SCHEDULE_ID_1 = "TestDeleteMeetingScheduleID1";
	static final String TIMESLOT_ID_1 = "TestDeleteMeetingTimeslotID1";
	static final String MEETING_NAME_1 = "TestDeleteMeetingName1";
	static final String SECRET_CODE_1 = "TestDeleteMeetingSecretCode1";
	
	// Test Meeting 2
	static final String MEETING_ID_2 = "TestDeleteMeetingID2";
	static final String SCHEDULE_ID_2 = "TestDeleteMeetingScheduleID2";
	static final String TIMESLOT_ID_2 = "TestDeleteMeetingTimeslotID2";
	static final String MEETING_NAME_2 = "TestDeleteMeetingName2";
	static final String SECRET_CODE_2 = "TestDeleteMeetingSecretCode2";
	
	// Test Timeslot
//	use TIMESLOT_ID_2 for timeslotID
//	use SCHEDULE_ID_2 for scheduleID
	static final int WEEK = 2;
	static final int DAY_IN_WEEK = 2;
	static final int SLOT_NUM_IN_DAY = 2;
	
	String meetingID = "coD5eJYE6M";
	String secretCode = "PnySiWt0Wd";
	
	@Test
	public void testDAODeleteMeeting()
	{
		MeetingDAO dao = new MeetingDAO();
		Meeting meeting = new Meeting(MEETING_ID_1, SCHEDULE_ID_1, TIMESLOT_ID_1, MEETING_NAME_1, SECRET_CODE_1);
		
		// Need to add a meeting to the database before it can be deleted, no?
		try
		{
			dao.addMeeting(meeting);
		}
		catch (Exception e)
		{
			fail ("addMeeting failed :" + e.getMessage());
		}
		
		// Confirm the meeting was added
		try
		{
			Meeting gotMeeting = dao.getMeeting(MEETING_ID_1);
			assertNotNull(gotMeeting);
		}
		catch (Exception e)
		{
			fail ("getMeeting failed: " + e.getMessage());
		}
		
		// Delete the meeting from the database
		try
		{
			boolean deleteBool = dao.deleteMeeting(MEETING_ID_1);
			assertTrue(deleteBool);
		}
		catch (Exception e)
		{
			fail ("addMeeting failed: " + e.getMessage());
		}
		
		// Confirm the meeting was removed
		try
		{
			Meeting gotMeeting = dao.getMeeting(MEETING_ID_1);
			assertNull(gotMeeting);
		}
		catch (Exception e)
		{
			fail ("getMeeting failed: " + e.getMessage());
		}
	}
	
	@Test
	public void testDeleteMeeting() throws IOException
	{
		TimeslotDAO timeslotDAO = new TimeslotDAO();
		LocalDateTime START_TIME = LocalDateTime.of(2018, Month.DECEMBER, 10, 10, 30, 0);
		Timeslot timeslot = new Timeslot(TIMESLOT_ID_2, SCHEDULE_ID_2, WEEK, DAY_IN_WEEK, SLOT_NUM_IN_DAY,
				START_TIME, false, true);
		MeetingDAO meetingDAO = new MeetingDAO();
		Meeting meeting = new Meeting(MEETING_ID_2, SCHEDULE_ID_2, TIMESLOT_ID_2, MEETING_NAME_2, SECRET_CODE_2);
	
		// Delete any Timeslot with this TimeslotID
		try
		{
			timeslotDAO.deleteTimeslot(TIMESLOT_ID_2);
		}
		catch(Exception e)
		{
			System.out.println("This should have done this, unless the timeslot already existed");
		}
		
		// Add the Timeslot to which the meeting will belong
		try
		{
			timeslotDAO.addTimeslot(timeslot);
		}
		catch(Exception e)
		{
			fail ("createTimeslot failed: " + e.getMessage());
		}
		
		// Add the meeting that will be deleted
		try
		{
			meetingDAO.addMeeting(meeting);
		}
		catch(Exception e)
		{
			System.out.println("This meeting already exists.  This does not negatively effect the test");
		}
		
		DeleteMeetingHandler handler = new DeleteMeetingHandler();
		JsonParser parser = new JsonParser();
		
		//create sample Json
		JsonObject input = new JsonObject();
		input.addProperty("meetingID", MEETING_ID_2);
		input.addProperty("secretCode", SECRET_CODE_2);
		
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