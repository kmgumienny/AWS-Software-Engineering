package test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.Test;

import main.database.MeetingDAO;
import main.database.ScheduleDAO;
import main.database.TimeslotDAO;
import main.entities.Meeting;
import main.entities.Schedule;
import main.entities.Timeslot;

public class TestThings {

//	@Test
//	public void testFind() {
//	    ConstantsDAO cd = new ConstantsDAO();
//	    try {
//	    	Constant c = cd.getConstant("e");
//	    	System.out.println("constant " + c.name + " = " + c.value);
//	    } catch (Exception e) {
//	    	fail ("didn't work:" + e.getMessage());
//	    }
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void CreateSceduleOnly() {
//		ScheduleDAO cd = new ScheduleDAO();
//	    try {
//	    	Schedule schedule = new Schedule("hi", "2018-12-03", "2018-12-04", 6, 11, 10);
//	    	boolean b = cd.addSchedule(schedule);
//	    	System.out.println("Create new schedule in database: " + b);
//	    } catch (Exception e) {
//	    	fail ("didn't work:" + e.getMessage());
//	    }
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void CreateTimeslotOnly() {
//		LocalDateTime localDateTime = LocalDateTime.of(2017, Month.AUGUST, 3, 12, 30);
//		
//		TimeslotDAO cd = new TimeslotDAO();
//	    try {
//	    	Timeslot timeslot = new Timeslot("adskfjsadk", localDateTime.toLocalDate(), localDateTime, false, true);
//	    	boolean b = cd.addTimeslot(timeslot);
//	    	System.out.println("Create new timeslot in database: " + b);
//	    	
//	    } catch (Exception e) {
//	    	fail ("didn't work:" + e.getMessage());
//	    }
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void createMeetingOnly() {
//		MeetingDAO cd = new MeetingDAO();
//		
//    	try {
//    		Meeting meeting = new Meeting("JOB", "LOST", "joblost");
//	    	boolean b = cd.addMeeting(meeting);
//	    	System.out.println("Create new meeting in database: " + b);
//	    } catch (Exception e) {
//	    	fail ("didn't work:" + e.getMessage());
//	    }
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void TestIfDateParsingWorks() {
//		//ScheduleDAO cd = new ScheduleDAO();
//	    try {
//	    	// can add it
//	    	//String id = UUID.randomUUID().toString().substring(0, 20); // no more than 20 because of DB restrictions...
//	    	Schedule schedule = new Schedule("hi", "2018-12-03", "2018-12-04", 6, 11, 10);
//	    	//schedule.setEndDate(schedule.getEndDate().plusDays(1));
//			long dailyTime = (schedule.getDayEndTime() - schedule.getDayStartTime())*60;
//			long numTimeslotsPerDay = dailyTime/schedule.getTimeSlotDuration();
//			long numDays= ChronoUnit.DAYS.between(schedule.getStartDate(), schedule.getEndDate());
//	    	//boolean b = cd.addSchedule(schedule);
//	    	System.out.println("dailyTime: " + dailyTime);
//	    	System.out.println("numTimeslotsPerDay: " + numTimeslotsPerDay);
//	    	System.out.println("numDays: " + numDays);
//	    	
//	    } catch (Exception e) {
//	    	fail ("didn't work:" + e.getMessage());
//	    }
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void CreateScheduleWithTimeslots() {
//		ScheduleDAO cd = new ScheduleDAO();
//	    try {
//	    	Schedule schedule = new Schedule("hi", "2018-12-03", "2018-12-12", 6, 8, 60);
//	    	boolean b = cd.addSchedule(schedule);
//	    	System.out.println("Create new schedule in database: " + b);
//	    	createTimeslots(schedule.getScheduleID(), schedule.getStartDate(), schedule.getEndDate(), schedule.getDayStartTime(), schedule.getDayEndTime(), schedule.getTimeSlotDuration());
//	    	
//	    } catch (Exception e) {
//	    	fail ("didn't work:" + e.getMessage());
//	    }
//	}
//
//	void createTimeslots(String scheduleID, LocalDate startDate, LocalDate endDate, int startTime, int endTime, int duration) {
//		TimeslotDAO tdao = new TimeslotDAO(); 
//		
//		long dailyTime = (endTime - startTime)*60;
//		long numTimeslotsPerDay = dailyTime/duration;
//		long numDays= ChronoUnit.DAYS.between(startDate, endDate);
//		
//		LocalDate itterationDate = startDate;
//		LocalTime sTime = LocalTime.of(startTime, 0);
//		
//		for (int i = 0; i < (int) numDays; i++)
//		{
//			if (itterationDate.getDayOfWeek().name() == "SATURDAY" || itterationDate.getDayOfWeek().name() == "SUNDAY")
//				itterationDate = itterationDate.plusDays(1);
//			
//			else {
//				for (long j = 0; j < numTimeslotsPerDay; j++)
//				{
//					Timeslot ts = new Timeslot(scheduleID, itterationDate, LocalDateTime.of(itterationDate, sTime), false, true);
//					try {
//						System.out.println("datetime: " + LocalDateTime.of(itterationDate, sTime));
//						boolean ans = tdao.addTimeslot(ts);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					sTime = sTime.plusMinutes(duration);
//				}	
//				itterationDate = itterationDate.plusDays(1);
//			}
//		}
//		
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void getScheduleWithID() {
//		ScheduleDAO cd = new ScheduleDAO();
//		String scheduleID = "nRIlGp3Wgi";
//		
//	    try {
//	    	Schedule schedule = cd.getSchedule(scheduleID);
//	    	System.out.println("Schedule with ID: " + schedule.getScheduleID() + " and with a name of: " + schedule.getScheduleName() + " has start date of: " + schedule.getStartDate() + " start time of: " + schedule.getDayStartTime() + " and slot duration of: " + schedule.getTimeSlotDuration());
//		    } catch (Exception e) {
//		    	System.out.println("Couldn't find the schedule with ID: " + scheduleID);
//		    	fail ("didn't work:" + e.getMessage());
//		    }
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void deleteScheduleWithID() {
//		ScheduleDAO cd = new ScheduleDAO();
//		String scheduleID = "nRIlGp3Wgi";
//		
//	    try {
//	    	boolean worked = cd.deleteSchedule(scheduleID);
//	    	System.out.println("Schedule with ID was deleted: " + worked);
//		    } catch (Exception e) {
//		    	System.out.println("Couldn't delete the schedule with ID: " + scheduleID);
//		    	fail ("didn't work:" + e.getMessage());
//		    }
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void getTimeslotWithTimeslotID() {
//		TimeslotDAO cd = new TimeslotDAO();
//		String TimeslotID = "lmevIGIhMe";
//		
//	    try {
//	    	Timeslot timeslot = cd.getTimeslot(TimeslotID);
//	    	System.out.println("Timeslot was retrieved with ID: " + timeslot.getTimeslotID());
//		    } catch (Exception e) {
//		    	System.out.println("Couldn't find timeslot with ID: " + TimeslotID);
//		    	fail ("didn't work:" + e.getMessage());
//		    }
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void getTimeslotWithScheduleID() {
//		TimeslotDAO cd = new TimeslotDAO();
//		String scheduleID = "adskfjsadk";
//		
//		try {
//			Timeslot timeslot = cd.getTimeslotWithScheduleID(scheduleID);
//			System.out.println("Timeslot was retrieved with scheduleID: " + timeslot.getScheduleID());
//		} catch (Exception e) {
//			System.out.println("Couldn't find timeslot with scheduleID: " + scheduleID);
//			fail ("didn't work:" + e.getMessage());
//		}
//	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void deleteTimeslotWithTimeslotID() {
//		TimeslotDAO cd = new TimeslotDAO();
//		String TimeslotID = "lmevIGIhMe";
//		
//		try {
//			boolean worked = cd.deleteTimeslot(TimeslotID);
//			System.out.println("Timeslot was deleted: " + worked);
//		} catch (Exception e) {
//			System.out.println("Couldn't delete timeslot with ID: " + TimeslotID);
//			fail ("didn't work:" + e.getMessage());
//		}
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void deleteTimeslotWithScheduleID() {
//	TimeslotDAO cd = new TimeslotDAO();
//	String scheduleID = "adskfjsadk";
//	
//		try {
//			boolean worked = cd.deleteTimeslotWithScheduleID(scheduleID);
//			System.out.println("Timeslot was deleted: " + worked);
//		} catch (Exception e) {
//			System.out.println("Couldn't delete timeslot with ID: " + scheduleID);
//			fail ("didn't work:" + e.getMessage());
//		}
//	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	@Test
//	public void deleteScheduleAndTimeslots() {
//	TimeslotDAO cd = new TimeslotDAO();
//	String scheduleID = "b4ok55kEYZ";
//    
//		try {
//			boolean worked = cd.deleteTimeslotWithScheduleID(scheduleID);
//			System.out.println("Timeslot was deleted: " + worked);
//		} catch (Exception e) {
//			System.out.println("Couldn't delete timeslot with ID: " + scheduleID);
//			fail ("didn't work:" + e.getMessage());
//		}
//		
//		
//		ScheduleDAO cd2 = new ScheduleDAO();
//		
//	    try {
//	    	boolean worked = cd2.deleteSchedule(scheduleID);
//	    	System.out.println("Schedule with ID was deleted: " + worked);
//	    } catch (Exception e) {
//	    	System.out.println("Couldn't delete the schedule with ID: " + scheduleID);
//	    	fail ("didn't work:" + e.getMessage());
//	    }
//	}
	
	
}
