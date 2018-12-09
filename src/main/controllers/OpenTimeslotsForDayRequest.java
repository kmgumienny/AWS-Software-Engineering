package main.controllers;

public class OpenTimeslotsForDayRequest {
	String scheduleID;
	String originizerSecretCode;
	String openDate;
	
	public OpenTimeslotsForDayRequest (String scheduleID, String originizerSecretCode, String openDate) {
		this.scheduleID = scheduleID;
		this.originizerSecretCode = originizerSecretCode;
		this.openDate = openDate;
	}
	
	public String toString() {
		return "Open(Timeslots for following date will be opened:" + openDate + " in the schedule with ID of: " + scheduleID +" and with secret code of: timeSlotID)";
	}
}
