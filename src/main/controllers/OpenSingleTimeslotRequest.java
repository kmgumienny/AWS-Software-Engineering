package main.controllers;


/*
 * From HTML:
 * data["scheduleID"] = arg1;
 */


public class OpenSingleTimeslotRequest {
	String timeSlotID;
	String originizerSecretCode;
	
	public OpenSingleTimeslotRequest (String timeSlotID, String originizerSecretCode) {
		this.timeSlotID = timeSlotID;
		this.originizerSecretCode = originizerSecretCode;
	}
	
	public String toString() {
		return "Close(Timeslot with ID of:" + timeSlotID + " and with secret code of: timeSlotID)";
	}
}
