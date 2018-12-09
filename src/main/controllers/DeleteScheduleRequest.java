package main.controllers;


/*
 * From HTML:
 * data["scheduleID"] = arg1;
 */


public class DeleteScheduleRequest {
	String scheduleID;
	String originizerSecretCode;
	
	public DeleteScheduleRequest (String scheduleID, String originizerSecretCode) {
		this.scheduleID = scheduleID;
		this.originizerSecretCode = originizerSecretCode;
	}
	
	public String toString() {
		return "Close(Schedule with ID of:" + scheduleID + " and with secret code of: timeSlotID)";
	}
}
