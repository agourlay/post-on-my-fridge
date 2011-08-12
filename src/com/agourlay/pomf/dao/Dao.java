package com.agourlay.pomf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.agourlay.pomf.model.Post;

public enum Dao {

	INSTANCE;

	public List<Post> listPosts() {
		EntityManager em = EMFService.get().createEntityManager();
		// Read the existing entries
		Query q = em.createQuery("select m from Post m");
		List<Post> posts = q.getResultList();
		return posts;
	}

	public void add(String author, String content) {
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Post Post = new Post(author,content);
			em.persist(Post);
			em.close();
		}
	}

	public List<Post> getPosts() {
		List<Post> posts = new ArrayList<Post>();
		EntityManager em = EMFService.get().createEntityManager();
		Query q = em.createQuery("select t from Post t ");
		posts = q.getResultList();
		return posts;
	}

	public void remove(long id) {
		EntityManager em = EMFService.get().createEntityManager();
		try {
			Post post = em.find(Post.class, id);
			em.remove(post);
		} finally {
			em.close();
		}
	}
}

