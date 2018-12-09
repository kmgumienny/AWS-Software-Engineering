package main.controllers;


/*
 * From HTML:
 * data["scheduleID"] = arg1;
 */


public class CloseTimelsotsForTimeRequest {
	String scheduleID;
	String originizerSecretCode;
	int hour;
	int minute;
	
	
	public CloseTimelsotsForTimeRequest (String scheduleID, String originizerSecretCode, int hour, int minute) {
		this.scheduleID = scheduleID;
		this.originizerSecretCode = originizerSecretCode;
		this.hour = hour;
		this.minute = minute;
	}
	
	public String toString() {
		return "Close(Timeslots for following time will be closed:" + hour + ":" + minute + " in the schedule with ID of: " + scheduleID +" and with secret code of: timeSlotID)";
	}
}
