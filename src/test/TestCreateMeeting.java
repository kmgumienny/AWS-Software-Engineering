package test;

import static org.junit.Assert.assertEquals;
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

import main.controllers.CreateMeetingHandler;
import main.controllers.CreateScheduleHandler;
import main.database.MeetingDAO;
import main.database.TimeslotDAO;
import main.entities.Meeting;
import main.entities.Timeslot;

public class TestCreateMeeting
{
	// Test meeting 1
	static final String MEETING_ID_1 = "TestCreateMeetingID1";
	static final String SCHEDULE_ID_1 = "TestCreateMeetingScheduleID1";
	static final String TIMESLOT_ID_1 = "TestCreateMeetingTimeslotID1";
	static final String MEETING_NAME_1 = "DAN ARNITZ";
	static final String SECRET_CODE_1 = "MutualFamilyFriend";
	
	// Test meeting 2
	static final String MEETING_ID_2 = "TestCreateMeetingID2";
	static final String SCHEDULE_ID_2 = "TestCreateMeetingScheduleID2";
	static final String TIMESLOT_ID_2 = "TestCreateMeetingTimeslotID2";
	static final String MEETING_NAME_2 = "Fo'of";
	static final String SECRET_CODE_2 = "Fa'af";
	
	// Test Timeslot
//	use TIMESLOT_ID_2 for timeslotID
//	use SCHEDULE_ID_2 for scheduleID
	static final int WEEK = 2;
	static final int DAY_IN_WEEK = 2;
	static final int SLOT_NUM_IN_DAY = 2;
	
	@Test
	public void testDAOCreateMeeting()
	{
		MeetingDAO dao = new MeetingDAO();
		Meeting meeting = new Meeting(MEETING_ID_1, SCHEDULE_ID_1, TIMESLOT_ID_1, MEETING_NAME_1, SECRET_CODE_1);
		
		// need to make sure that the meeting with this ID does not already exist
		try
		{
			dao.deleteMeeting(MEETING_ID_1);
		}
		catch (Exception e)
		{
			fail ("deleteMeeting failed :" + e.getMessage());
		}
		
		// add the meeting to the database
		try
		{
			boolean addBool = dao.addMeeting(meeting);
			assertTrue(addBool);
		}
		catch (Exception e)
		{
			fail ("addMeeting failed: " + e.getMessage());
		}
		
		// check the database meeting with this ID and compare it to local parameters
		try
		{
			Meeting gotMeeting = dao.getMeeting(MEETING_ID_1);
			assertEquals(gotMeeting.getScheduleID(), SCHEDULE_ID_1);
			assertEquals(gotMeeting.getTimeslotID(), TIMESLOT_ID_1);
			assertEquals(gotMeeting.getMeetingName(), MEETING_NAME_1);
			assertEquals(gotMeeting.getSecretCode(), SECRET_CODE_1);
		}
		catch (Exception e)
		{
			fail ("getMeeting failed: " + e.getMessage());
		}
	}
	
	@Test
	public void testCreateMeetingHandler() throws IOException
	{
		TimeslotDAO timeslotDAO = new TimeslotDAO();
		LocalDateTime START_TIME = LocalDateTime.of(2018, Month.DECEMBER, 10, 10, 30, 0);
		Timeslot timeslot = new Timeslot(TIMESLOT_ID_2, SCHEDULE_ID_2, WEEK, DAY_IN_WEEK, SLOT_NUM_IN_DAY,
				START_TIME, false, true);
		MeetingDAO meetingDAO = new MeetingDAO();
	
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
		
		// make sure a meeting with this ID does not already exist
		try
		{
			meetingDAO.deleteMeeting(MEETING_ID_2);
		}
		catch (Exception e)
		{
			fail ("deleteMeeting failed: " + e.getMessage());
		}
//			
//			// add the meeting to the database
//			try
//			{
//				dao.addMeeting(meeting);
//			}
//			catch (Exception e)
//			{
//				fail ("addMeeting failed: " + e.getMessage());
//			}
		
		CreateMeetingHandler handler = new CreateMeetingHandler();
		JsonParser parser = new JsonParser();
		
		//create sample Json
		JsonObject input = new JsonObject();
		input.addProperty("meetingID", MEETING_ID_2);
		input.addProperty("scheduleID", SCHEDULE_ID_2);
		input.addProperty("timeslotID", TIMESLOT_ID_2);
		input.addProperty("meetingName", MEETING_NAME_2);
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
		
		// test case
		assertEquals(200, intCode);
	}
}
