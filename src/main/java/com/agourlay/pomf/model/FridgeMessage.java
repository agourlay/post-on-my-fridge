package com.agourlay.pomf.model;

import java.io.Serializable;

import lombok.Data;

import org.joda.time.DateTime;

@Data public class FridgeMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String command;
	private String user;
	private String message;
	private Long timestamp;

	public FridgeMessage(String command, String user, String message) {
		this.command = command;
		this.user = user;
		this.message = message;
		this.timestamp = DateTime.now().getMillis();
	}
}
