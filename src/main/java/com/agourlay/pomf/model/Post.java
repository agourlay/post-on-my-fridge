package com.agourlay.pomf.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

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
public class Post implements Serializable {

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
		this.date = new DateTime();
	}

	// GETTERS & SETTERS

	public Long getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public DateTime getDate() {
		return date;
	}

	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	public void setDate(DateTime date) {
		this.date = date;
	}

	public Double getPositionX() {
		return positionX;
	}

	public void setPositionX(Double positionX) {
		this.positionX = positionX;
	}

	public Double getPositionY() {
		return positionY;
	}

	public void setPositionY(Double positionY) {
		this.positionY = positionY;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	public DateTime getDueDate() {
		return dueDate;
	}

	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	public void setDueDate(DateTime dueDate) {
		this.dueDate = dueDate;
	}

	public String getFridgeId() {
		return fridgeId;
	}

	public void setFridgeId(String fridgeId) {
		this.fridgeId = fridgeId;
	}

}