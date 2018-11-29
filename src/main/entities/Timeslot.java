package main.entities;

import java.sql.Date;
import java.sql.Time;

public class Timeslot
{
	String timeslotID;
	Date date;
	Time startTime;
	int duration;
	boolean isReserved;
	boolean isOpen;
	
	public Timeslot(String timeslotID, Date date, Time startTime, boolean isReserved, boolean isOpen)
	{
		this.timeslotID = timeslotID;
		this.date = date;
		this.startTime = startTime;
		this.isReserved = isReserved;
		this.isOpen = isReserved;
	}
	
	// Getters
	public String getTimeslotID()
	{
		return timeslotID;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	public Time getStartTime()
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
	
	public void setDate(Date newDate)
	{
		date = newDate;
	}
	
	public void setStartTime(Time newTime)
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
