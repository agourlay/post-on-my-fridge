package com.agourlay.pomf.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.FridgeUser;
import com.agourlay.pomf.model.Post;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;

/**
 * DAO Objectify
 * 
 */
public class ObjectifyDao<T> extends DAOBase {
	
 protected Class<T> clazz;

	static {
		ObjectifyService.register(Post.class);
		ObjectifyService.register(Fridge.class);
		ObjectifyService.register(FridgeUser.class);
	}
	
	public ObjectifyDao(Class<T> clazz)
	{
		this.clazz = clazz;
	}

	public Key<T> put(T entity)

	{
		return ofy().put(entity);
	}

	public Map<Key<T>, T> putAll(Iterable<T> entities)
	{
		return ofy().put(entities);
	}

	public void delete(T entity)
	{
		ofy().delete(entity);
	}

	public void deleteKey(Key<T> entityKey)
	{
		ofy().delete(entityKey);
	}

	public void deleteAll(Iterable<T> entities)
	{
		ofy().delete(entities);
	}

	public void deleteKeys(Iterable<Key<T>> keys)
	{
		ofy().delete(keys);
	}

	public T get(Long id) throws EntityNotFoundException
	{
		return ofy().get(this.clazz, id);
	}

	public T get(Key<T> key) throws EntityNotFoundException
	{
		return ofy().get(key);
	}

	/**
	 * Convenience method to get all objects matching a single property
	 *
	 * @param propName
	 * @param propValue
	 * @return T matching Object
	 */
	public T getByProperty(String propName, Object propValue)
	{
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		return q.get();
	}

	public List<T> listByProperty(String propName, Object propValue)
	{
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		return asList(q.fetch());
	}

	public List<Key<T>> listKeysByProperty(String propName, Object propValue)
	{
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		return asKeyList(q.fetchKeys());
	}

	private List<T> asList(Iterable<T> iterable)
	{
		ArrayList<T> list = new ArrayList<T>();
		for (T t : iterable)
		{
			list.add(t);
		}
		return list;
	}

	private List<Key<T>> asKeyList(Iterable<Key<T>> iterableKeys)
	{
		ArrayList<Key<T>> keys = new ArrayList<Key<T>>();
		for (Key<T> key : iterableKeys)
		{
			keys.add(key);
		}
		return keys;
	}
}
