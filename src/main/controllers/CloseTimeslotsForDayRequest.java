package main.controllers;


/*
 * From HTML:
 * data["scheduleID"] = arg1;
 */


public class CloseTimeslotsForDayRequest {
	String scheduleID;
	String organizerSecretCode;
	String closeDate;
	
	public CloseTimeslotsForDayRequest (String scheduleID, String organizerSecretCode, String closeDate) {
		this.scheduleID = scheduleID;
		this.organizerSecretCode = organizerSecretCode;
		this.closeDate = closeDate;
	}
	
	public String toString() {
		return "Close(Timeslots for following date will be closed:" + closeDate + " in the schedule with ID of: " + scheduleID +" and with secret code of: timeSlotID)";
	}
}
