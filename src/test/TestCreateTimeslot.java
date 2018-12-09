package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;

import main.database.TimeslotDAO;
import main.entities.Timeslot;

public class TestCreateTimeslot
{
	static final String TIMESLOT_ID_1 = "CreateTimeslotTest1";
	static final String SCHEDULE_ID_1 = "CreateTimeslotTestSchedule1";
	static final int WEEK_1 = 1;
	static final int DAY_IN_WEEK_1 = 3;
	static final int SLOT_NUM_IN_DAY = 2;
	
	
	@Test
	public void testDAOCreateTimeslot()
	{
		LocalDateTime START_TIME = LocalDateTime.of(2018, Month.DECEMBER, 5, 12, 30, 0);
		
		TimeslotDAO dao = new TimeslotDAO();
		Timeslot timeslot = new Timeslot(TIMESLOT_ID_1, SCHEDULE_ID_1, WEEK_1, DAY_IN_WEEK_1, SLOT_NUM_IN_DAY, START_TIME, false, true);
		
		// need to make sure that there is no timeslot with the same ID
		try
		{
			dao.deleteTimeslot(TIMESLOT_ID_1);
		}
		catch (Exception e)
		{
			fail ("deleteTimeslot failed: " + e.getMessage());
		}

		// Add the timeslot to the database
		try
		{
			boolean addBool = dao.addTimeslot(timeslot);
			assertTrue(addBool);
		}
		catch (Exception e)
		{
			fail ("addTimeslot failed: " + e.getMessage());
		}

		// Check the timeslot in the database, and compare it to the local parameters
		try
		{
			Timeslot gotTimeslot = dao.getTimeslot(TIMESLOT_ID_1);
			
			assertEquals(gotTimeslot.getScheduleID(), SCHEDULE_ID_1);
			assertEquals(gotTimeslot.getWeek(), WEEK_1);
			assertEquals(gotTimeslot.getDayInWeek(), DAY_IN_WEEK_1);
			assertEquals(gotTimeslot.getStartTime().toString(), START_TIME.toString());
			assertFalse(gotTimeslot.getIsReserved());
			assertTrue(gotTimeslot.getIsOpen());
		}
		catch (Exception e)
		{
			fail ("getTimeslot failed: " + e.getMessage());
		}
	}
}
