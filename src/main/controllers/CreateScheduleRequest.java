package main.controllers;


/*
 * From HTML:
 * data["scheduleName"] = arg1;
 * data["startDate"] = arg2;
 * data["endDate"] = arg3;
 * data["startTime"] = arg4;
 * data["endTime"] = arg5;
 * data["increment"] = arg6;
 */


public class CreateScheduleRequest {
	String scheduleName;
	String startDate;
	String endDate;
	int dailyStartTime;
	int dailyEndTime;
	int timeSlotDuration;
	
	public CreateScheduleRequest (String scheduleName, String startDate, String endDate, int dailyStartTime, int dailyEndTime, int timeSlotDuration) {
		this.scheduleName = scheduleName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dailyStartTime = dailyStartTime;
		this.dailyEndTime = dailyEndTime;
		this.timeSlotDuration = timeSlotDuration;
	}
	
	public String toString() {
		return "Create(" + scheduleName + " from " + startDate + " to " + endDate + " starting at " + dailyStartTime + " till " + dailyEndTime + " in increments of " + timeSlotDuration + " minutes)";
	}
}
