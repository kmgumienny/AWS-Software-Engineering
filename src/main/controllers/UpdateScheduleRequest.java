package main.controllers;

public class UpdateScheduleRequest
{
	String scheduleName;
	String scheduleID;
	String secretCode;
	String startDate;
	String endDate;
	int startTime;
	int endTime;
	int increment;
	
	public UpdateScheduleRequest(String scheduleName, String startDate, String endDate, int startTime, int endTime, int increment)
	{
		this.scheduleName = scheduleName;
		this.scheduleID = scheduleID;
		this.secretCode = secretCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.increment = increment;
	}
	
	public String toString()
	{
		return "Update(" + scheduleName + " to start on " + startDate + " and end on " + endDate + " starting at " + startTime + " till " + endTime + " in increments of " + increment + " minutes)";
	}
}