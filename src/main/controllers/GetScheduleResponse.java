package main.controllers;

import java.util.List;

import main.entities.Timeslot;

public class GetScheduleResponse {
	String message;
	List<Timeslot> TimeSlots;
	int httpCode;
	
	public GetScheduleResponse (String message, int code) {
		this.message = message;
		this.TimeSlots = null;
		this.httpCode = code;
	}
	
	// 200 means success
	public GetScheduleResponse (String message, List<Timeslot> TimeSlots) {
		this.message = message;
		this.TimeSlots = TimeSlots;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
