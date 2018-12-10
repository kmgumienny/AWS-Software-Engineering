package main.controllers;


/*
 * From HTML:
 * data["scheduleID"] = arg1;
 */


public class CloseSingleTimeslotRequest {
	String timeSlotID;
	String organizerSecretCode;
	
	public CloseSingleTimeslotRequest (String timeSlotID, String organizerSecretCode) {
		this.timeSlotID = timeSlotID;
		this.organizerSecretCode = organizerSecretCode;
	}
	
	public String toString() {
		return "Close(Timeslot with ID of:" + timeSlotID + " and with secret code of: timeSlotID)";
	}
}
