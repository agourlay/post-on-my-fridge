package com.agourlay.pomf.model;

import java.io.Serializable;

import lombok.Data;

import org.joda.time.DateTime;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
@Data public class Stat implements Serializable {

	private static final long serialVersionUID = 1025020018320400913L;

	@Id
	private Long id;
	private DateTime generationDate;
	private Integer fridgeNumber;
	private Integer postNumber;

	public Stat() {
	}
}
