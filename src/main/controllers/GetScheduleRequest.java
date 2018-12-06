package main.controllers;


/*
 * From HTML:
 * data["scheduleID"] = arg1;
 */


public class GetScheduleRequest {
	String scheduleID;
	
	public GetScheduleRequest (String scheduleID) {
		this.scheduleID = scheduleID;
	}
	
	public String toString() {
		return "Get(Schedule with ID of:" + scheduleID + ")";
	}
}
