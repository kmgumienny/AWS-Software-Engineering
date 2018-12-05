package main.controllers;

import main.entities.Schedule;

public class GetScheduleResponse
{
	String respMessage;
	String scheduleName;
	String scheduleID;
	String secretCode;
	String startDate;
	String endDate;
	int startTime;
	int endTime;
	int increment;
	int httpCode;
	
	// 200 means success
	public GetScheduleResponse(Schedule schedule)
	{
		this.scheduleName = schedule.getScheduleName();
		this.scheduleID = schedule.getScheduleID();
		this.secretCode = schedule.getSecretCode();
		this.startDate = schedule.getStartDate().toString();
		this.endDate = schedule.getEndDate().toString();
		this.startTime = schedule.getDayStartTime();
		this.endTime = schedule.getDayEndTime();
		this.increment = schedule.getTimeSlotDuration();

		this.respMessage = "Successful Retrieval of Schedule";
		this.httpCode = 200;
	}
	
	// not 200 means failure
	public GetScheduleResponse(String message, int code)
	{
		this.respMessage = message;
		this.httpCode = code;
	}
	
	
	public String toString()
	{
		return "Response(" + respMessage + ")";
	}
}
