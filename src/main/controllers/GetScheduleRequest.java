package main.controllers;

public class GetScheduleRequest
{
	//2) Create a lambda function that takes a string with secret key, gets schedule from rds, gets the schedule's id and
	//gets timeslots, throws timeslots in a list, and then sends all the details to the front end in the form of strings, 
	//ints, and the list of timeslots


	/*
	 * From HTML:
	 * data["scheduleName"] = arg1;
	 * data["startDate"] = arg2;
	 * data["endDate"] = arg3;
	 * data["startTime"] = arg4;
	 * data["endTime"] = arg5;
	 * data["increment"] = arg6;
	 */
	
	String scheduleID;
	
	public GetScheduleRequest (String scheduleID)
	{
		this.scheduleID = scheduleID;
	}
	
	public String toString() {
		return "Get(Schedule with ID " + scheduleID + ")";
	}
}
