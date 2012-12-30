package com.agourlay.pomf.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheService.SetPolicy;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class CacheService {

	private static MemcacheService cache = MemcacheServiceFactory.getMemcacheService();

	public static void put(String key, Object Value) {
		cache.put(key, Value, Expiration.byDeltaSeconds(3600), SetPolicy.SET_ALWAYS);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getList(String key) {
		List<T> returned = (List<T>) cache.get(key);
		return (returned == null) ? Collections.EMPTY_LIST : returned;
	}

	@SuppressWarnings("unchecked")
	public static synchronized <T> void addToList(String key, T value) {
		List<T> addElement = new ArrayList<T>();
		addElement.add(value);
		if (cache.get(key) != null) {
			addElement.addAll((Collection<? extends T>) cache.get(key));
		}
		cache.put(key, addElement, Expiration.byDeltaSeconds(1440), SetPolicy.SET_ALWAYS);
	}

}
