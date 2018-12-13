package main.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Schedule
{
	String scheduleName;
	String scheduleID;
	String secretCode; // Code used by Organizer to allow for edits to be made, not inputted, created during instantiation
	LocalDate startDate;
	LocalDate endDate;
	int dayStartTime;
	int dayEndTime;
	int timeSlotDuration; // No. of minutes in a timeslot
	LocalDateTime creationDate;
	
	
	public Schedule(String scheduleName, String startDate, String endDate, int dayStartTime, int dayEndTime, int timeSlotDuration, LocalDateTime creationDate){
		this.scheduleName = scheduleName;
		this.scheduleID = genRandString(10);
		this.secretCode = genRandString(10);
		this.startDate = parseDate(startDate);
		this.endDate = parseDate(endDate);
		this.dayStartTime = dayStartTime;
		this.dayEndTime = dayEndTime;
		this.timeSlotDuration = timeSlotDuration;
		this.creationDate = creationDate;
	}
	
	public Schedule(String scheduleName, String scheduleID, String secretCode, LocalDate startDate, LocalDate endDate, int dayStartTime, int dayEndTime, int timeSlotDuration, LocalDateTime creationDate){
		this.scheduleName = scheduleName;
		this.scheduleID = scheduleID;
		this.secretCode = secretCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dayStartTime = dayStartTime;
		this.dayEndTime = dayEndTime;
		this.timeSlotDuration = timeSlotDuration;
		this.creationDate = creationDate;
	}
	
	LocalDate parseDate(String date) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dtf);
	  }
	
///////////////////////////////////////////////////////////////////////////////////////
	
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
	
///////////////////////////////////////////////////////////////////////////////////////
	
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
	
	public LocalDate getStartDate()
	{
		return startDate;
	}
	
	public LocalDate getEndDate()
	{
		return endDate;
	}
	
	public int getDayStartTime()
	{
		return dayStartTime;
	}
	
	public int getDayEndTime()
	{
		return dayEndTime;
	}
	
	public int getTimeSlotDuration()
	{
		return timeSlotDuration;
	}
	
	public LocalDateTime getCreationDate()
	{
		return creationDate;
	}
	
	// end Getters //

///////////////////////////////////////////////////////////////////////////////////////
	
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
	
	public void setStartDate(LocalDate newDate)
	{
		startDate = newDate;
	}
	
	public void setEndDate(LocalDate newDate)
	{
		endDate = newDate;
	}
	
	public void setDayStartTime(int newTime)
	{
		dayStartTime = newTime;
	}
	
	public void setDayEndTime(int newTime)
	{
		dayEndTime = newTime;
	}
	
	public void setTimeSlotDuration(int newDuration)
	{
		timeSlotDuration = newDuration;
	}

}
