package main.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public class Timeslot
{
	String timeslotID;
	String scheduleID;
	int week;
	LocalDate date;
	LocalDateTime startTime;
	boolean isReserved;
	boolean isOpen;
	
	public Timeslot(String timeslotID, String scheduleID, int week, LocalDate date, LocalDateTime startTime, boolean isReserved, boolean isOpen)
	{
		this.timeslotID = timeslotID;
		this.scheduleID = scheduleID;
		this.week = week;
		this.date = date;
		this.startTime = startTime;
		this.isReserved = isReserved;
		this.isOpen = isOpen;
	}
	
	public Timeslot(String scheduleID, int week, LocalDate date, LocalDateTime startTime, boolean isReserved, boolean isOpen)
	{
		this.timeslotID = genRandString(10);
		this.scheduleID = scheduleID;
		this.week = week;
		this.date = date;
		this.startTime = startTime;
		this.isReserved = isReserved;
		this.isOpen = isOpen;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Getters
	public String getTimeslotID()
	{
		return timeslotID;
	}
	
	public String getScheduleID()
	{
		return scheduleID;
	}
	
	public int getWeek()
	{
		return week;
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
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Setters
	public void setTimeslotID(String newID)
	{
		timeslotID = newID;
	}
	
	public void setScheduleID(String newID)
	{
		scheduleID = newID;
	}
	
	public void setWeek(int newWeek)
	{
		week = newWeek;
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
