package main.controllers;


/*
 * From HTML:
 * data["scheduleID"] = arg1;
 */


public class ExtandEndDateRequest {
	String scheduleID;
	String organizerSecretCode;
	String newDate;
	
	
	public ExtandEndDateRequest (String scheduleID, String organizerSecretCode, String newDate) {
		this.scheduleID = scheduleID;
		this.organizerSecretCode = organizerSecretCode;
		this.newDate = newDate;
	}
	
	public String toString() {
		return "Extand(Extand the ending date for the schedule to: " + newDate + ", in the schedule with ID of: " + scheduleID +" and with secret code of: timeSlotID)";
	}
}
