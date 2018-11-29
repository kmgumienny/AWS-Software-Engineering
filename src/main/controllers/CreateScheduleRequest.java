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
	int startTime;
	int endTime;
	int increment;
	
	public CreateScheduleRequest (String scheduleName, String startDate, String endDate, int startTime, int endTime, int increment) {
		this.scheduleName = scheduleName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.increment = increment;
	}
	
	public String toString() {
		return "Create(" + scheduleName + " from " + startDate + " to " + endDate + " starting at " + startTime + " till " + endTime + " in increments of " + increment + " minutes)";
	}
}
