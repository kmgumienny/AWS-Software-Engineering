package main.controllers;

public class DeleteMeetingResponse {
	String message;
	int httpCode;
	
	public DeleteMeetingResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public DeleteMeetingResponse (String message) {
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
