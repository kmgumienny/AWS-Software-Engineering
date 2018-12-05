package main.controllers;


public class UpdateScheduleResponse
{
	String message;
	int httpCode;
	
	// NOTE: No Data gets sent to Frontend upon Update Schedule
	public UpdateScheduleResponse()
	{
		message = "Schedule Successfully Updated";
		httpCode = 200;
	}
	
	public UpdateScheduleResponse(String message, int code)
	{
		this.message = message;
		this.httpCode = code;
	}
	
	public String toString()
	{
		return "Response(" + httpCode + ": " + message + ")";
	}
}
