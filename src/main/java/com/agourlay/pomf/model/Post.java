package com.agourlay.pomf.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;

import com.agourlay.pomf.service.ClientRepository;
import com.agourlay.pomf.tools.Constantes;
import com.agourlay.pomf.tools.CustomDateTimeDeserializer;
import com.agourlay.pomf.tools.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
@XmlRootElement
public class Post implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5300832520775741740L;

	@Id private Long id;
	private String author;
	private String content;
	private String color;
	private DateTime date;
	private Double positionX;
	private Double positionY;
	private DateTime dueDate;
	@Index private String fridgeId;

	public Post() {	this.date = new DateTime(); }
	
	// DAO METHODS 

	public static void savePost(Post post){
		ofy().save().entity(post).now();
        ClientRepository.notifyAllClientFromFridge(post.getFridgeId(),Constantes.COMMAND_REFRESH,null,null);
	}
			
	public static Post getPostById(Long id) {
		return ofy().load().type(Post.class).id(id).get();
	}
	
	public static List<Post> getAllPost() {
        return ofy().load().type(Post.class).limit(10000).list();
	}
	
	public static List<Post> getPostByFridge(String fridgeName) {
        return ofy().load().type(Post.class).filter("fridgeId", fridgeName).limit(100).list();
	}
	
	public static void remove(long id) {
		Post post = getPostById(id);
		String currentFridgeId = post.getFridgeId();
		if (post != null){
			ofy().delete().entity(post).now();
			ClientRepository.notifyAllClientFromFridge(currentFridgeId,Constantes.COMMAND_REFRESH,null,null);
		}
	}
	
	public static void remove(Collection<Long> ids){
		for (Long id : ids){
			remove(id);
		}
	}
	
	public static int countPost(){
		return ofy().load().type(Post.class).count();
	}
	
	//GETTERS & SETTERS
	
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
	
	@JsonSerialize(using=CustomDateTimeSerializer.class)
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
	
	@JsonSerialize(using=CustomDateTimeSerializer.class)
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