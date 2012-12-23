package com.agourlay.pomf.model;

public class ChatMessage {

	private String command;
	private String user;
	private String message;

	public ChatMessage(String command, String user, String message) {
		this.command = command;
		this.user = user;
		this.message = message;
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

}
