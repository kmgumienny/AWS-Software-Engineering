package main.controllers;

public class GetOldSchedulesRequest {
	int hoursPassed;
	
	public GetOldSchedulesRequest (int hoursPassed) {
		this.hoursPassed = hoursPassed;
	}
	
	public String toString() {
		return "Get(Schedules older than " + hoursPassed + " hours)";
	}
}
