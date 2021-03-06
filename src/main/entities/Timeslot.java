package main.entities;

import java.time.LocalDateTime;
import java.util.Random;

public class Timeslot
{
	String timeslotID;
	String scheduleID;
	int week;
	int dayInWeek;                   // 1 (Monday) to 7 (Sunday)
	int slotNumInDay;
	LocalDateTime startTime;
	boolean isReserved;
	boolean isOpen;
	
	public Timeslot(String timeslotID, String scheduleID, int week, int dayInWeek, int slotNumInDay, LocalDateTime startTime, boolean isReserved, boolean isOpen)
	{
		this.timeslotID = timeslotID;
		this.scheduleID = scheduleID;
		this.week = week;
		this.dayInWeek = dayInWeek;
		this.slotNumInDay = slotNumInDay;
		this.startTime = startTime;
		this.isReserved = isReserved;
		this.isOpen = isOpen;
	}
	
	public Timeslot(String scheduleID, int week, int dayInWeek, int slotNumInDay, LocalDateTime startTime, boolean isReserved, boolean isOpen)
	{
		this.timeslotID = genRandString(10);
		this.scheduleID = scheduleID;
		this.week = week;
		this.dayInWeek = dayInWeek;
		this.slotNumInDay = slotNumInDay;
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
	
	public int getDayInWeek()
	{
		return dayInWeek;
	}
	
	public int getSlotNumInDay()
	{
		return slotNumInDay;
	}
	
	public int getWeek()
	{
		return week;
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
	
	public void setDayInWeek(int newDayInWeek)
	{
		dayInWeek = newDayInWeek;
	}
	
	public void setSlotNumInDay(int newSlotNumInDay)
	{
		slotNumInDay = newSlotNumInDay;
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
