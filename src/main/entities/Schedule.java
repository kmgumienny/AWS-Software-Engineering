package main.entities;

import java.sql.Time;
import java.util.List;
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
	
	@SuppressWarnings("deprecation")
	List<Timeslot> createTimeSlots(Date startDate, Date endDate, Time startTime, Time endTime,
									int duration)
	{
		long dailyTime = endTime.getTime() - startTime.getTime();
		long longDuration = (long) duration;
		long numTimeSlotsPerDay = dailyTime/longDuration;
		
		// TODO: There must be a better way to check the number of days in between two dates
		//	The 31 is exclusively because the getMonth assumes 31 days in a month
		int numDays = 0;
		if (endDate.getYear() != startDate.getYear())
		{
			numDays += 31 * (endDate.getMonth()+1); //Add 1 to account for Jan returning 0 in getMonth
		}
		if (endDate.getMonth() != startDate.getMonth())
		{
			numDays += endDate.getMonth() + (31 - startDate.getDate());
		}
		numDays = endDate.getDate() - startDate.getDate();
		
		return null;
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
