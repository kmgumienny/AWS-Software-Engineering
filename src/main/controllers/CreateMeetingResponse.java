package main.controllers;

public class CreateMeetingResponse {
	String message;
	String meetingName;
	String meetingID;
	String secretCode;
	int httpCode;
	
	public CreateMeetingResponse (String message, int code) {
		this.message = message;
		this.meetingName = null;
		this.meetingID = null;
		this.secretCode = null;
		this.httpCode = code;
	}
	
	// 200 means success
	public CreateMeetingResponse (String message, String meetingName, String meetingID, String secretCode) {
		this.message = message;
		this.meetingName = meetingName;
		this.meetingID = meetingID;
		this.secretCode = secretCode;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
