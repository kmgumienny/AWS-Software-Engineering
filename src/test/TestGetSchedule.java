package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import main.database.ScheduleDAO;
import main.entities.Schedule;

public class TestGetSchedule
{
	static final String SCHEDULE_NAME = "Test Schedule";
	static final String SCHEDULE_ID = "TestId12345";
	static final String SECRET_CODE = "TestCode321";
	static final String START_DATE = "2018-12-05";
	static final String END_DATE = "2018-12-14";
	static final int DAY_START_TIME = 10;
	static final int DAY_END_TIME = 14;
	static final int DURATION = 10;
	
	@Test
	public void testDAOGetSchedule()
	{
		ScheduleDAO dao = new ScheduleDAO();
		Schedule schedule = null;
		createSchedule();
	    try
	    {
	    	schedule = dao.getSchedule(SCHEDULE_ID);
	    } catch (Exception e)
	    {
	    	fail ("didn't work:" + e.getMessage());
	    }
	    
	    assertEquals(schedule.getScheduleID(), SCHEDULE_ID);
	    assertEquals(schedule.getScheduleName(), SCHEDULE_NAME);
	    assertEquals(schedule.getSecretCode(), SECRET_CODE);
	    assertEquals(schedule.getDayStartTime(), DAY_START_TIME);
	    assertEquals(schedule.getDayEndTime(), DAY_END_TIME);
	    assertEquals(schedule.getTimeSlotDuration(), DURATION);
	}
	
	@Test
	public void testLambdaGetSchedule()
	{
		
	}
	public void createSchedule()
	{
		ScheduleDAO dao = new ScheduleDAO();
		try
		{
		dao.addSchedule(new Schedule(SCHEDULE_NAME, SCHEDULE_ID,
				SECRET_CODE, START_DATE, END_DATE,
				DAY_START_TIME, DAY_END_TIME, DURATION));
		} catch (Exception e)
		{
			fail ("did not add schedule " + e.getMessage());
		}
	}
}
