package main.controllers;

public class DeleteOldSchedulesRequest {
	int daysPassed;
	
	public DeleteOldSchedulesRequest (int daysPassed) {
		this.daysPassed = daysPassed;
	}
	
	public String toString() {
		return "Get(Schedules older than " + daysPassed + " days)";
	}
}
