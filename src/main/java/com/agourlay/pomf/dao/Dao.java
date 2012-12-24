package com.agourlay.pomf.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.FridgeMessage;
import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.service.ClientService;
import com.agourlay.pomf.util.Constantes;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class Dao {

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

	public static void savePost(Post post) {
		ofy().save().entity(post).now();
		ClientService.notifyClientsFromFridge(post.getFridgeId(), new FridgeMessage(Constantes.COMMAND_REFRESH, null, null,
				new DateTime()));
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

	public static void deletePost(long id) {
		Post post = getPostById(id);
		if (post != null) {
			String currentFridgeId = post.getFridgeId();
			ofy().delete().entity(post).now();
			ClientService.notifyClientsFromFridge(currentFridgeId, new FridgeMessage(Constantes.COMMAND_REFRESH, null, null,
					new DateTime()));
		}
	}

	public static void deletePosts(Collection<Long> ids) {
		for (Long id : ids) {
			deletePost(id);
		}
	}

	public static int countPost() {
		return ofy().load().type(Post.class).count();
	}

}
