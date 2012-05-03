package com.agourlay.pomf.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.agourlay.pomf.dao.ObjectifyDao;
import com.agourlay.pomf.tools.Constantes;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@Entity
public class Fridge implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String name;
	private FridgeUser owner;	
	
	//DAO METHODS
	
	@SuppressWarnings("unchecked")
	public static List<Post> getPosts(String fridgeName) {
		 MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
         List<Post> posts = (List<Post>) cache.get(Constantes.CACHE_FRIDGE_KEY+fridgeName);
         if (posts == null) {
                 ObjectifyDao dao = new ObjectifyDao();
                 posts = dao.ofy().query(Post.class).filter("fridgeId", fridgeName).order("-date").limit(100).list();
                 if (posts != null)
                         cache.put(Constantes.CACHE_FRIDGE_KEY+fridgeName, posts); 
         }
         return posts;
	}
	
	//GETTERS & SETTERS
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FridgeUser getOwner() {
		return owner;
	}

	public void setOwner(FridgeUser owner) {
		this.owner = owner;
	}
}
