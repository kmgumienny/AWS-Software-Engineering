package main.entities;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.sql.Date;

public class Schedule
{
	String scheduleName;
	String scheduleID;
	String secretCode; // Code used by Organizer to allow for edits to be made, not inputted, 
					   //	created during instantiation
	Date startDate;
	Date endDate;
	Time dayStartTime;
	Time dayEndTime;
	int timeSlotDuration; // No. of minutes in a timeslot
	List<Timeslot> timeslots;

	
	public Schedule(String scheduleName, String scheduleID, String secretCode, Date startDate, Date endDate,
						Time dayStartTime, Time dayEndTime, int timeSlotDuration)
	{
		this.scheduleName = scheduleName;
		this.scheduleID = scheduleID;
		this.secretCode = secretCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dayStartTime = dayStartTime;
		this.dayEndTime = dayEndTime;
		this.timeSlotDuration = timeSlotDuration;
	}
	
	ArrayList<Timeslot> createTimeSlots(Date startDate, Date endDate, Time startTime, Time endTime,
									int duration)
	{
		ArrayList<Timeslot> timeslots = new ArrayList<Timeslot>();
		
		long dailyTime = endTime.getTime() - startTime.getTime();
		long longDuration = (long) duration;
		long numTimeSlotsPerDay = dailyTime/longDuration;
		
		int numDays = 2; // TODO: Change this to be variable based on the startDate and endDate.
		
		for (int i = 0; i < numDays; i++)
		{
			for (long j = 0; j < numTimeSlotsPerDay; j++)
			{
				Date newDate = new Date(startDate.getTime() + (long) i*3600000);
				Time newTime = new Time(startTime.getTime() + j*longDuration);
				timeslots.add(new Timeslot(genRandString(10), newDate, newTime, duration));
			}
		}
			
		
		// TODO: There must be a better way to check the number of days in between two dates
		//	The 31 is exclusively because the getMonth assumes 31 days in a month
		
		return timeslots;
	}
	
	private String genRandString(int length)
	{
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random rand = new Random();
		StringBuilder tempString = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			tempString.append(characters.charAt(rand.nextInt(characters.length())));
		}
		return tempString.toString();
	}
	
	// Getters //
	public String getScheduleName()
	{
		return scheduleName;
	}
	
	public String getScheduleID()
	{
		return scheduleID;
	}
	
	// I refuse to have an explicit getter for the secretCode
	public String getSecretCode()
	{
		return secretCode;
	}
	
	public Date getStartDate()
	{
		return startDate;
	}
	
	public Date getEndDate()
	{
		return endDate;
	}
	
	public Time getDayStartTime()
	{
		return dayStartTime;
	}
	
	public Time getDayEndTime()
	{
		return dayEndTime;
	}
	
	public int getTimeSlotDuration()
	{
		return timeSlotDuration;
	}
	// end Getters //
	
	// Setters
	public void setScheduleName(String newName)
	{
		scheduleName = newName;
	}
	
	public void setScheduleID(String newID)
	{
		scheduleID = newID;
	}
	
	public void setSecretCode(String newCode)
	{
		secretCode = newCode;
	}
	
	public void setStartDate(Date newDate)
	{
		startDate = newDate;
	}
	
	public void setEndDate(Date newDate)
	{
		endDate = newDate;
	}
	
	public void setDayStartTime(Time newTime)
	{
		dayStartTime = newTime;
	}
	
	public void setDayEndTime(Time newTime)
	{
		dayEndTime = newTime;
	}
	
	public void setTimeSlotDuration(int newDuration)
	{
		timeSlotDuration = newDuration;
	}
	// end Setters
}
