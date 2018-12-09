package main.controllers;

import java.util.List;

import main.entities.Meeting;
import main.entities.Schedule;
import main.entities.Timeslot;

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
