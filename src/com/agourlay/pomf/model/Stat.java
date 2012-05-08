package com.agourlay.pomf.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import com.agourlay.pomf.dao.ObjectifyDao;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;

@Entity
@Cached
public class Stat implements Serializable{

	private static final long serialVersionUID = 1025020018320400913L;
	private static final ObjectifyDao<Stat> dao = new ObjectifyDao<Stat>(Stat.class);
	
	
	@Id
	private Long id;
	private Date generationDate;
	private Integer fridgeNumber;
	private Integer PostNumber;
	
	public static List<Stat> getAllStats(){
	    List<Stat> stats = dao.ofy().query(Stat.class).limit(1000).list();
		return stats;
	}
	
	public static void createStat(Stat stat){
		dao.put(stat);
	}
	
	public static void generateDailyStat(){
		Stat newStat = new Stat();
		newStat.setGenerationDate(new Date());
		newStat.setFridgeNumber(Fridge.countFridge());
		newStat.setPostNumber(Post.countPost());
		Stat.createStat(newStat);
	}
	
	/* GETTERS & SETTERS */
	
	public Integer getPostNumber() {
		return PostNumber;
	}
	public void setPostNumber(Integer postNumber) {
		PostNumber = postNumber;
	}
	public Date getGenerationDate() {
		return generationDate;
	}
	public void setGenerationDate(Date generationDate) {
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
