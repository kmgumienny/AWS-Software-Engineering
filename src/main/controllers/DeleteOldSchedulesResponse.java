package main.controllers;

import java.util.List;

import main.entities.Schedule;


public class DeleteOldSchedulesResponse {
	String message;
	int numDeleted;
	int httpCode;
	
	public DeleteOldSchedulesResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public DeleteOldSchedulesResponse (int numDeleted, String message) {
		this.numDeleted = numDeleted;
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
