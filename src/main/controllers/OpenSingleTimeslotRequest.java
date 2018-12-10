package main.controllers;


/*
 * From HTML:
 * data["scheduleID"] = arg1;
 */


public class OpenSingleTimeslotRequest {
	String timeSlotID;
	String organizerSecretCode;
	
	public OpenSingleTimeslotRequest (String timeSlotID, String originizerSecretCode) {
		this.timeSlotID = timeSlotID;
		this.organizerSecretCode = originizerSecretCode;
	}
	
	public String toString() {
		return "Close(Timeslot with ID of:" + timeSlotID + " and with secret code of: timeSlotID)";
	}
}
