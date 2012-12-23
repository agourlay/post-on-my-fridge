package com.agourlay.pomf.model;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

@Entity
@Cache
public class Fridge implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6461491688659114651L;

	@Id
	private String name;
	private String description;
	@Load
	private List<Post> posts;

	public Fridge() {
	}

	// DAO METHODS
	public static Fridge getFridgeById(String fridgeId) {
		return ofy().load().type(Fridge.class).id(fridgeId).get();
	}

	public static int countFridge() {
		return ofy().load().type(Fridge.class).count();
	}

	public static Long createFridge(String fridgeId) {
		Fridge newFridge = new Fridge();
		newFridge.setName(fridgeId);
		return ofy().save().entity(newFridge).now().getId();
	}

	public static void createFridgeIfNotExist(String fridgeId) {
		if (getFridgeById(fridgeId) == null) {
			createFridge(fridgeId);
		}
	}

	public static List<Fridge> searchFridgeLike(String fridgeName) {
		return ofy().load().type(Fridge.class).filter("name >=", fridgeName).filter("name <", fridgeName + "\uFFFD").list();
	}

	// TODO : get the names back using an objectify request, we don't need the
	// full fridge objects.
	public static List<String> searchFridgeNamesWithNameLike(String fridgeName) {
		return Lists.transform(searchFridgeLike(fridgeName), new Function<Fridge, String>() {
			@Override
			public String apply(Fridge fridge) {
				return fridge.getName();
			}
		});
	}

	public static List<Fridge> getAllFridge() {
		return ofy().load().type(Fridge.class).limit(10000).list();
	}

	public static void saveFridge(Fridge fridge) {
		ofy().save().entity(fridge).now();
	}

	// GETTERS & SETTERS

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Post> getPosts() {
		return Post.getPostByFridge(this.name);
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

}
