package main.controllers;

import java.util.List;

import main.entities.Meeting;
import main.entities.Schedule;
import main.entities.Timeslot;

public class GetOldSchedulesResponse {
	String message;
	List<Schedule> schedules;
	int httpCode;
	
	public GetOldSchedulesResponse (String message, int code) {
		this.message = message;
		this.schedules = null;
		this.httpCode = code;
	}
	
	// 200 means success
	public GetOldSchedulesResponse (String message, List<Schedule> schedules) {
		this.message = message;
		this.schedules = schedules;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
