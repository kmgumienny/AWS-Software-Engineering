package main.controllers;

public class OpenTimeslotsForTimeResponse {
	String message;
	int httpCode;
	
	public OpenTimeslotsForTimeResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public OpenTimeslotsForTimeResponse (String message) {
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
