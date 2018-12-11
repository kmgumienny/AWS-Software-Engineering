package main.controllers;


/*
 * From HTML:
 * data["meetingID"] = arg1;
 */


public class DeleteMeetingByOrginizerRequest {
	String meetingID;
	String organizerSecretCode;
	
	public DeleteMeetingByOrginizerRequest (String meetingID, String organizerSecretCode) {
		this.meetingID = meetingID;
		this.organizerSecretCode = organizerSecretCode;
	}
	
	public String toString() {
		return "Get(Delete meeting with ID of:" + meetingID + " and with orginizer secrete code of:" + organizerSecretCode + ")";
	}
}
