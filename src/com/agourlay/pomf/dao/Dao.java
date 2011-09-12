package com.agourlay.pomf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.agourlay.pomf.model.Post;

public enum Dao {

	INSTANCE;

	public void add(String author, String content) {
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Post Post = new Post(author,content);
			em.persist(Post);
			em.close();
		}
	}

	public void updatePosition(Long id, Double positionX,Double positionY) {
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Post postToUpdate = em.find(Post.class, id);
			if (postToUpdate != null){
				postToUpdate.setPositionX(positionX);
				postToUpdate.setPositionY(positionY);
				em.persist(postToUpdate);
			}
			em.close();
		}
	}
	
	public List<Post> getPosts() {
		synchronized (this) {
			List<Post> posts = new ArrayList<Post>();
			EntityManager em = EMFService.get().createEntityManager();
			Query q = em.createQuery("select t from Post t order by date ASC");
			posts = q.getResultList();
			return posts;
		}
	}
	
	public Post getPostById(Long id) {
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			Post post = em.find(Post.class, id);
			return post;
		}
	}
	

	public void remove(long id) {
		synchronized (this) {
			EntityManager em = EMFService.get().createEntityManager();
			try {
				Post post = em.find(Post.class, id);
				em.remove(post);
			} finally {
				em.close();
			}
		}
	}
}

