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
	
	public Timeslot(String timeslotID, Date date, Time startTime, int duration)
	{
		this.timeslotID = timeslotID;
		this.date = date;
		this.startTime = startTime;
		this.duration = duration;
		isReserved = false;
		isOpen = true;
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
	
	public int getDuration()
	{
		return duration;
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
	
	public void setDuration(int newDuration)
	{
		duration = newDuration;
	}
	// end Setters
}
