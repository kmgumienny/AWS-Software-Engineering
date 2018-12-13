package main.controllers;

import java.util.List;

import main.entities.Schedule;


public class DeleteOldSchedulesResponse {
	String message;
	List<Schedule> schedules;
	int httpCode;
	
	public DeleteOldSchedulesResponse (String message, int code) {
		this.message = message;
		this.schedules = null;
		this.httpCode = code;
	}
	
	// 200 means success
	public DeleteOldSchedulesResponse (String message, List<Schedule> schedules) {
		this.message = message;
		this.schedules = schedules;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
