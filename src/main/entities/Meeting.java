package main.entities;

import java.util.Random;

public class Meeting
{
	String meetingID;
	String scheduleID;
	String timeslotID;
	String meetingName;
	String secretCode;
	
	
	public Meeting(String scheduleID, String timeslotID, String meetingName)
	{
		this.meetingID = genRandString(10);
		this.scheduleID = scheduleID;
		this.timeslotID = timeslotID;
		this.meetingName = meetingName;
		this.secretCode = genRandString(10);
	}
	
	public Meeting(String meetingID, String scheduleID, String timeslotID, String meetingName, String secretCode)
	{
		this.meetingID = meetingID;
		this.scheduleID = scheduleID;
		this.timeslotID = timeslotID;
		this.meetingName = meetingName;
		this.secretCode = secretCode;
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
	
	
	// Getters
	public String getMeetingID()
	{
		return meetingID;
	}
	
	public String getScheduleID()
	{
		return scheduleID;
	}
	
	public String getTimeslotID()
	{
		return timeslotID;
	}
	
	public String getMeetingName()
	{
		return meetingName;
	}
	
	public String getSecretCode()
	{
		return secretCode;
	}
	// end getters
	
	// Setters
	public void setMeetingID(String newMeetingIDID)
	{
		meetingID = newMeetingIDID;
	}
	
	public void setScheduleID(String newScheduleID)
	{
		scheduleID = newScheduleID;
	}
	
	public void setTimeslotID(String newTimeslotID)
	{
		timeslotID = newTimeslotID;
	}
	
	public void setName(String newName)
	{
		meetingName = newName;
	}
	
	public void setSecretCode(String newCode)
	{
		secretCode = newCode;
	}
	// end setters
}
