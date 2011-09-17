package com.agourlay.pomf.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.agourlay.pomf.tools.Validation;

/**
 * Model class which will store the Post Items
 * 
 * 
 */
@Entity
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String author;
	private String content;
	private Date date;
	private Double positionX;
	private Double positionY;

	public Post(String author, String content, Double positionX,Double positionY) {
		
		if (Validation.isNoTNullOrEmpty(author)){
			this.author = author.substring(0, 1).toUpperCase() + author.substring(1).toLowerCase();
		}else{
			this.author = "Anonymous";
		}
		
		if (Validation.isNoTNullOrEmpty(content)){
			this.content = content;
		}else{
			this.content = "What's up?";
		}
		
		this.date = new Date();
		this.positionX = positionX;
		this.positionY = positionY;
	}

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
	
	public Date getDate() {
		return date;
	}
	
	public String getFormatedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return sdf.format(date);
	}

	public void setDate(Date date) {
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

}