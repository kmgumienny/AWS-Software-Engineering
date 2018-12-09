package main.controllers;

public class OpenSingleTimeslotResponse {
	String message;
	int httpCode;
	
	public OpenSingleTimeslotResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public OpenSingleTimeslotResponse (String message) {
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
