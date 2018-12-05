package main.controllers;


/*
 * From HTML:
 * data["timeslotID"] = arg1;
 * data["meetingName"] = arg2;
 */


public class CreateMeetingRequest {
	String timeslotID;
	String meetingName;
	
	public CreateMeetingRequest (String scheduleID, String meetingName) {
		this.timeslotID = scheduleID;
		this.meetingName = meetingName;
	}
	
	public String toString() {
		return "Create(Timeslot ID of:" + timeslotID + " With meeitng name of: " + meetingName + ")";
	}
}
