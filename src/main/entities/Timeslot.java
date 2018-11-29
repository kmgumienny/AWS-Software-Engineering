package main.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Timeslot
{
	String timeslotID;
	String scheduleID;
	LocalDate date;
	LocalDateTime startTime;
	int duration;
	boolean isReserved;
	boolean isOpen;
	
	public Timeslot(String timeslotID, String scheduleID, LocalDate date, LocalDateTime startTime, boolean isReserved, boolean isOpen)
	{
		this.timeslotID = timeslotID;
		this.scheduleID = scheduleID;
		this.date = date;
		this.startTime = startTime;
		this.isReserved = isReserved;
		this.isOpen = isReserved;
	}
	
	public Timeslot(String timeslotID, String scheduleID, LocalDate date, boolean isReserved, boolean isOpen)
	{
		this.timeslotID = timeslotID;
		this.scheduleID = scheduleID;
		this.date = date;
		this.startTime = LocalDateTime.now();
		this.isReserved = isReserved;
		this.isOpen = isReserved;
	}
	
	// Getters
	public String getTimeslotID()
	{
		return timeslotID;
	}
	
	public String getScheduleID()
	{
		return scheduleID;
	}
	
	public LocalDate getDate()
	{
		return date;
	}
	
	public LocalDateTime getStartTime()
	{
		return startTime;
	}
	
	public boolean getIsReserved()
	{
		return isReserved;
	}
	
	public boolean getIsOpen()
	{
		return isOpen;
	}
	// end Getters
	
	// Setters
	public void setTimeslotID(String newID)
	{
		timeslotID = newID;
	}
	
	public void setScheduleID(String newID)
	{
		scheduleID = newID;
	}
	
	public void setDate(LocalDate newDate)
	{
		date = newDate;
	}
	
	public void setStartTime(LocalDateTime newTime)
	{
		startTime = newTime;
	}
	
	public void setIsReserved(boolean newBool)
	{
		isReserved = newBool;
	}
	
	public void setIsOpen(boolean newBool)
	{
		isOpen = newBool;
	}
	// end Setters
}
