package com.agourlay.pomf.tools.predicates;

import java.util.Date;

import com.agourlay.pomf.model.Post;
import com.google.common.base.Predicate;

public class DuePost implements Predicate<Post>{

	@Override
	public boolean apply(Post post) {
		return post.getDueDate() != null && post.getDueDate().before(new Date())? true :false;
	}

}
