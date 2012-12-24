package com.agourlay.pomf.model;

import org.joda.time.DateTime;

public class FridgeMessage {

	private String command;
	private String user;
	private String message;
	private DateTime date;

	public FridgeMessage(String command, String user, String message, DateTime date) {
		this.command = command;
		this.user = user;
		this.message = message;
		this.date = date;
	}

	// GETTERS & SETTERS

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

}
