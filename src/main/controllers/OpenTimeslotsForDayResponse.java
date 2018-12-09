package main.controllers;

public class OpenTimeslotsForDayResponse {
	String message;
	int httpCode;
	
	public OpenTimeslotsForDayResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public OpenTimeslotsForDayResponse (String message) {
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
