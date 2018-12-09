package main.controllers;

import java.util.List;

import main.entities.Meeting;
import main.entities.Schedule;
import main.entities.Timeslot;

public class CloseSingleTimeslotResponse {
	String message;
	int httpCode;
	
	public CloseSingleTimeslotResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public CloseSingleTimeslotResponse (String message) {
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
