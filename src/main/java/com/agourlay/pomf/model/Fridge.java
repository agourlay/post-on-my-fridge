package com.agourlay.pomf.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.agourlay.pomf.dao.ObjectifyDao;
import com.agourlay.pomf.tools.Constantes;
import com.agourlay.pomf.tools.transformer.ExtractFridgeName;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.common.collect.Lists;

@Entity
public class Fridge implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6461491688659114651L;
	
	private static final ObjectifyDao<Fridge> dao = new ObjectifyDao<Fridge>(Fridge.class);
	
	@Id
	private String name;
	private FridgeUser owner;	
	
	public Fridge() {}
	
	//DAO METHODS

	@SuppressWarnings("unchecked")
	public static List<Post> getPosts(String fridgeName) {
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
         	List<Post> posts = (List<Post>) cache.get(Constantes.CACHE_FRIDGE_KEY+fridgeName);
         	// if posts not in the cache, it's time to fetch
         	if (posts == null) {
        		 posts = dao.ofy().query(Post.class).filter("fridgeId", fridgeName).order("-date").limit(100).list();
                 	// if we have posts, we cache it
                 	if (posts != null)
                         	cache.put(Constantes.CACHE_FRIDGE_KEY+fridgeName, posts); 
         	}
         	return posts;
	}
	
	public static Fridge getFridgeById(String fridgeId){
		return dao.ofy().query(Fridge.class).filter("name", fridgeId).get();
	}
	
	public static int countFridge(){
		return dao.count();
	}
	
	public static Fridge createFridge(String fridgeId){
		Fridge newFridge = new Fridge();
		newFridge.setName(fridgeId);
		dao.put(newFridge);
		return newFridge;
	}
	
	public static void createFridgeIfNotExist(String fridgeId) {
        	if (getFridgeById(fridgeId) == null) {
        		createFridge(fridgeId);
          	}
	}


	public static List<Fridge> searchFridgeLike(String fridgeName){
		return dao.ofy().query(Fridge.class).filter("name >=", fridgeName).filter("name <", fridgeName + "\uFFFD").list(); 
	}
	
	public static List<String> searchFridgeNamesWithNameLike(String fridgeName){
		return Lists.transform(searchFridgeLike(fridgeName),  new ExtractFridgeName());
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
