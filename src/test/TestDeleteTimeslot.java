package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;

import main.database.TimeslotDAO;
import main.entities.Meeting;
import main.entities.Timeslot;

public class TestDeleteTimeslot
{
	// Test Timeslot
	static final String TIMESLOT_ID_1 = "TestDeleteTimeslotID1";
	static final String SCHEDULE_ID_1 = "TestDeleteTimeslotTestScheduleID1";
	static final int WEEK_1 = 1;
	static final int DAY_IN_WEEK_1 = 3;
	static final int SLOT_NUM_IN_DAY = 2;
	
	@Test
	public void testDAODeleteTimeslot()
	{
		LocalDateTime START_TIME = LocalDateTime.of(2018, Month.DECEMBER, 5, 12, 30, 0);
		
		TimeslotDAO dao = new TimeslotDAO();
		Timeslot timeslot = new Timeslot(TIMESLOT_ID_1, SCHEDULE_ID_1, WEEK_1, DAY_IN_WEEK_1, SLOT_NUM_IN_DAY, START_TIME, false, true);
		
		// Add the timeslot to be deleted to the database
		try
		{
			dao.addTimeslot(timeslot);
		}
		catch (Exception e)
		{
			System.out.println("A timeslot with ID identical to that which is trying to be added"
					+ " already exists.  This will not negatively impact the test.");
		}
		
		// Make sure the timeslot exists in the database
		try
		{
			Timeslot gotTimeslot = dao.getTimeslot(TIMESLOT_ID_1);
			assertNotNull(gotTimeslot);
		}
		catch (Exception e)
		{
			fail ("getMeeting failed: " + e.getMessage());
		}
		
		// Delete the timeslot from the database
		try
		{
			dao.deleteTimeslot(TIMESLOT_ID_1);
		}
		catch (Exception e)
		{
			fail ("deleteTimeslot failed: " + e.getMessage());
		}

		// Confirm the timeslot was deleted
		try
		{
			Timeslot gotTimeslot = dao.getTimeslot(TIMESLOT_ID_1);
			assertNull(gotTimeslot);
		}
		catch (Exception e)
		{
			fail ("getMeeting failed: " + e.getMessage());
		}
	}
}
