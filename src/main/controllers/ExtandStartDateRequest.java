package main.controllers;


/*
 * From HTML:
 * data["scheduleID"] = arg1;
 */


public class ExtandStartDateRequest {
	String scheduleID;
	String organizerSecretCode;
	String newStartDate;
	
	
	public ExtandStartDateRequest (String scheduleID, String organizerSecretCode, String newStartDate) {
		this.scheduleID = scheduleID;
		this.organizerSecretCode = organizerSecretCode;
		this.newStartDate = newStartDate;
	}
	
	public String toString() {
		return "Extand(Extand the starting date for the schedule to: " + newStartDate + ", in the schedule with ID of: " + scheduleID +" and with secret code of: timeSlotID)";
	}
}
