package main.controllers;

import java.util.List;

import main.entities.Meeting;
import main.entities.Schedule;
import main.entities.Timeslot;

public class GetScheduleResponse {
	String message;
	Schedule schedule;
	List<Timeslot> timeSlots;
	List<Meeting> meetings;
	int httpCode;
	
	public GetScheduleResponse (String message, int code) {
		this.message = message;
		this.timeSlots = null;
		this.meetings = null;
		this.httpCode = code;
	}
	
	// 200 means success
	public GetScheduleResponse (String message, Schedule schedule, List<Timeslot> timeSlots, List<Meeting> meetings) {
		this.message = message;
		this.schedule = schedule;
		this.timeSlots = timeSlots;
		this.meetings = meetings;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
