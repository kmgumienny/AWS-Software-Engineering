package main.controllers;

public class CreateScheduleResponse {
	String message;
	String ID;
	String secret;
	int httpCode;
	
	public CreateScheduleResponse (String s, int code) {
		this.message = s;
		this.ID = null;
		this.secret = null;
		this.httpCode = code;
	}
	
	// 200 means success
	public CreateScheduleResponse (String message, String id, String key) {
		this.message = message;
		this.ID = id;
		this.secret = key;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
