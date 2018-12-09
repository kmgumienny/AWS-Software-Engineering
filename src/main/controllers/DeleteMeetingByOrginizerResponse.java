package main.controllers;

public class DeleteMeetingByOrginizerResponse {
	String message;
	int httpCode;
	
	public DeleteMeetingByOrginizerResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public DeleteMeetingByOrginizerResponse (String message) {
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
