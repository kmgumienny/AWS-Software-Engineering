package main.controllers;


/*
 * From HTML:
 * data["meetingID"] = arg1;
 */


public class DeleteMeetingRequest {
	String meetingID;
	String secretCode;
	
	public DeleteMeetingRequest (String meetingID, String secretCode) {
		this.meetingID = meetingID;
		this.secretCode = secretCode;
	}
	
	public String toString() {
		return "Get(Delete meeting with ID of:" + meetingID + " and with secrete code of:" + secretCode + ")";
	}
}
