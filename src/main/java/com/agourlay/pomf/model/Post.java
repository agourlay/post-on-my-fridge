package com.agourlay.pomf.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import com.agourlay.pomf.dao.ObjectifyDao;
import com.agourlay.pomf.service.ClientRepository;
import com.agourlay.pomf.tools.Constantes;
import com.agourlay.pomf.tools.CustomDateTimeDeserializer;
import com.agourlay.pomf.tools.CustomDateTimeSerializer;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@Entity
public class Post implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5300832520775741740L;

	private static final ObjectifyDao<Post> dao = new ObjectifyDao<Post>(Post.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String author;
	private String content;
	private String color;
	private DateTime date;
	private Double positionX;
	private Double positionY;
	private DateTime dueDate;
	private String fridgeId;

	public Post() {
		this.date = new DateTime();
	}

	// DAO METHODS 

	public static void savePost(Post post){
        	dao.ofy().put(post);
        	MemcacheServiceFactory.getMemcacheService().delete(Constantes.CACHE_FRIDGE_KEY+post.getFridgeId());
        	ClientRepository.notifyAllClientFromFridge(post.getFridgeId(),Constantes.COMMAND_REFRESH,null,null);
	}
			
	public static Post getPostById(Long id) {
		try {
			return dao.get(id);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<Post> getAllPost() {
        List<Post> posts = dao.ofy().query(Post.class).order("-date").limit(1000).list();
		return posts;
	}
	
	public static void remove(long id) {
		Post post = getPostById(id);
		String currentFridgeId = post.getFridgeId();
		if (post != null){
			dao.delete(post);
			MemcacheServiceFactory.getMemcacheService().delete(Constantes.CACHE_FRIDGE_KEY+currentFridgeId);
			ClientRepository.notifyAllClientFromFridge(currentFridgeId,Constantes.COMMAND_REFRESH,null,null);
		}
	}
	
	public static void remove(Collection<Long> ids){
		for (Long id : ids){
			remove(id);
		}
	}
	
	public static int countPost(){
		return dao.count();
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