package main.controllers;

public class DeleteScheduleResponse {
	String message;
	int httpCode;
	
	public DeleteScheduleResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public DeleteScheduleResponse (String message) {
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
