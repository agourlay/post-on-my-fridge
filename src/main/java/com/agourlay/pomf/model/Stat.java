package com.agourlay.pomf.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

import com.agourlay.pomf.dao.Dao;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Stat implements Serializable {

	private static final long serialVersionUID = 1025020018320400913L;

	@Id
	private Long id;
	private DateTime generationDate;
	private Integer fridgeNumber;
	private Integer postNumber;

	public Stat() {
	}

	public static List<Stat> getAllStats() {
		return ofy().load().type(Stat.class).limit(1000).list();
	}

	public static void createStat(Stat stat) {
		ofy().save().entity(stat).now();
	}

	public static void generateDailyStat() {
		Stat newStat = new Stat();
		newStat.setGenerationDate(new DateTime());
		newStat.setFridgeNumber(Dao.countFridge());
		newStat.setPostNumber(Dao.countPost());
		Stat.createStat(newStat);
	}

	/* GETTERS & SETTERS */

	public Integer getPostNumber() {
		return postNumber;
	}

	public void setPostNumber(Integer postNumber) {
		this.postNumber = postNumber;
	}

	public DateTime getGenerationDate() {
		return generationDate;
	}

	public void setGenerationDate(DateTime generationDate) {
		this.generationDate = generationDate;
	}

	public Integer getFridgeNumber() {
		return fridgeNumber;
	}

	public void setFridgeNumber(Integer fridgeNumber) {
		this.fridgeNumber = fridgeNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
