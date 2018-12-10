package main.controllers;

public class ExtandEndDateResponse {
	String message;
	int httpCode;
	
	public ExtandEndDateResponse (String message, int code) {
		this.message = message;
		this.httpCode = code;
	}
	
	// 200 means success
	public ExtandEndDateResponse (String message) {
		this.message = message;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + message + ")";
	}
}
