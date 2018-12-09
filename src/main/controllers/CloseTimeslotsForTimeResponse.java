package main.controllers;

public class CloseTimeslotsForTimeResponse {
	String message;
	int httpCode;
	
	public CloseTimeslotsForTimeResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public CloseTimeslotsForTimeResponse (String message) {
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
