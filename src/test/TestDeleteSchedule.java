package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import main.database.ScheduleDAO;
import main.entities.Schedule;

public class TestDeleteSchedule
{
	// Test Schedule 1
	static final String SCHEDULE_NAME_1 = "DeleteScheduleTest1";
	static final String SCHEDULE_ID_1 = "DeleteScheduleTest1";
	static final String SECRET_CODE_1 = "54321";
	static final String START_DATE_1 = "2018-12-03";
	static final String END_DATE_1 = "2018-12-04";
	static final int START_TIME_1 = 6;
	static final int END_TIME_1 = 11;
	static final int INCREMENT_1 = 10;
	
	// Test Schedule 2
	static final String SCHEDULE_NAME_2 = "TestCreateScheduleTest2";
	static final String SCHEDULE_ID_2 = "CreateScheduleTest2";
	static final String SECRET_CODE_2 = "85201";
	static final String START_DATE_2 = "2018-12-03";
	static final String END_DATE_2 = "2018-12-04";
	static final int START_TIME_2 = 6;
	static final int END_TIME_2 = 11;
	static final int INCREMENT_2 = 10;
			
	@Test
	public void testDAODeleteSchedule()
	{
		ScheduleDAO dao = new ScheduleDAO();
		Schedule nullSchedule = null;
		
		// Have to create a schedule to delete it, no?
		try
		{
			Schedule schedule = new Schedule(SCHEDULE_NAME_1, SCHEDULE_ID_1, SECRET_CODE_1, START_DATE_1, END_DATE_1, START_TIME_1, END_TIME_1, INCREMENT_1);
			boolean addBool = dao.addSchedule(schedule);
			assertTrue(addBool);
		}
		catch (Exception e)
		{
			fail ("addSchedule failed: " + e.getMessage());
		}
		
		// Confirm the schedule was created and added to the database
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
		
		// Now delete the schedule that was just created
	    try
	    {
	    	boolean deleteBool = dao.deleteSchedule(SCHEDULE_ID_1);
	    	assertTrue(deleteBool);
	    }
	    catch (Exception e)
	    {
	    	fail ("deleteSchedule failed: " + e.getMessage());
	    }
	    
	    // just make sure that it was, in fact, deleted
	    try
	    {
	    	nullSchedule = dao.getSchedule(SCHEDULE_ID_1);
	    }
	    catch (Exception e)
	    {
	    	System.out.println("getSchedule failed: " + e.getMessage());
	    }
	    assertNull(nullSchedule);
	}
	
	@Test
	public void testDeleteScheduleHandler()
	{
		// TODO: when we get a deleteScheduleHandler created, heres a test function "framework" to test it!!
	}
}
