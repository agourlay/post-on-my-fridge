package com.agourlay.pomf.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;

@Entity
@Cache
@Data public class Fridge implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6461491688659114651L;

	@Id
	private String name;
	private String description;
	@Ignore
	private List<Post> posts;

	public Fridge() {
	}
}
