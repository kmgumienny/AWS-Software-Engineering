package main.controllers;

public class UpdateScheduleRequest
{
	String scheduleID;
	
	public UpdateScheduleRequest(String scheduleID)
	{
		this.scheduleID = scheduleID;
	}
	
	public String toString()
	{
		return "Update(Schedule with ID " + scheduleID + ")";
	}
}
