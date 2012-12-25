package com.agourlay.pomf.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

import org.joda.time.DateTime;

import com.agourlay.pomf.util.CustomDateTimeDeserializer;
import com.agourlay.pomf.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
@XmlRootElement
@Data public class Post implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5300832520775741740L;

	@Id
	private Long id;
	private String author;
	private String content;
	private String color;
	private DateTime date;
	private Double positionX;
	private Double positionY;
	private DateTime dueDate;
	@Index
	private String fridgeId;

	public Post() {
		this.date = DateTime.now();
	}
	
	@JsonSerialize(using=CustomDateTimeSerializer.class)
	public DateTime getDate() {
		return date;
	}
	@JsonDeserialize(using=CustomDateTimeDeserializer.class)
	public void setDate(DateTime date) {
		this.date = date;
	}
	@JsonSerialize(using=CustomDateTimeSerializer.class)
	public DateTime getDueDate() {
		return dueDate;
	}
	@JsonDeserialize(using=CustomDateTimeDeserializer.class)
	public void setDueDate(DateTime dueDate) {
		this.dueDate = dueDate;
	}
	
	
}