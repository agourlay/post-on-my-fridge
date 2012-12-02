package com.agourlay.pomf.model;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class FridgeUser implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3440636574318540914L;	
	
	@Id	private Long id;

	public FridgeUser() {}

	//GETTERS & SETTERS
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
