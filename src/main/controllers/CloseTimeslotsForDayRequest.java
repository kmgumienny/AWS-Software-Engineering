package main.controllers;


/*
 * From HTML:
 * data["scheduleID"] = arg1;
 */


public class CloseTimeslotsForDayRequest {
	String scheduleID;
	String originizerSecretCode;
	String closeDate;
	
	public CloseTimeslotsForDayRequest (String scheduleID, String originizerSecretCode, String closeDate) {
		this.scheduleID = scheduleID;
		this.originizerSecretCode = originizerSecretCode;
		this.closeDate = closeDate;
	}
	
	public String toString() {
		return "Close(Timeslots for following date will be closed:" + closeDate + " in the schedule with ID of: " + scheduleID +" and with secret code of: timeSlotID)";
	}
}
