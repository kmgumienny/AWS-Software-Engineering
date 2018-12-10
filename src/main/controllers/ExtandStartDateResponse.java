package main.controllers;

public class ExtandStartDateResponse {
	String message;
	int httpCode;
	
	public ExtandStartDateResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public ExtandStartDateResponse (String message) {
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
