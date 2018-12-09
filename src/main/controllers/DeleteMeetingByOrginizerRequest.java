package main.controllers;


/*
 * From HTML:
 * data["meetingID"] = arg1;
 */


public class DeleteMeetingByOrginizerRequest {
	String meetingID;
	String orginizerSecretCode;
	
	public DeleteMeetingByOrginizerRequest (String meetingID, String orginizerSecretCode) {
		this.meetingID = meetingID;
		this.orginizerSecretCode = orginizerSecretCode;
	}
	
	public String toString() {
		return "Get(Delete meeting with ID of:" + meetingID + " and with orginizer secrete code of:" + orginizerSecretCode + ")";
	}
}
