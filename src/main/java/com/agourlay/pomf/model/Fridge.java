package com.agourlay.pomf.model;

import java.io.Serializable;
import java.util.List;

import com.agourlay.pomf.dao.Dao;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

@Entity
@Cache
public class Fridge implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6461491688659114651L;

	@Id
	private String name;
	private String description;
	@Load
	private List<Post> posts;

	public Fridge() {
	}

	// GETTERS & SETTERS

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Post> getPosts() {
		return Dao.getPostByFridge(this.name);
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

}
