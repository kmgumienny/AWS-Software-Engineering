package main.entities;

public class Meeting
{
	String meetingID;
	String meetingName;
	String secretCode;
	
	public Meeting(String meetingID, String meetingName, String secretCode)
	{
		this.meetingID = meetingID;
		this.meetingName = meetingName;
		this.secretCode = secretCode;
	}
	
	// Getters
	public String getMeetingID()
	{
		return meetingID;
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
	public void setMeetingID(String newID)
	{
		meetingID = newID;
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
