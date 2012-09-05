package com.agourlay.pomf.tools.predicates;

import org.joda.time.DateTime;

import com.agourlay.pomf.model.Post;
import com.google.common.base.Predicate;

public class DuePost implements Predicate<Post>{

	@Override
	public boolean apply(Post post) {
		return post.getDueDate() != null && post.getDueDate().isBefore(new DateTime())? true :false;
	}

}
