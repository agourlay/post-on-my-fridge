package com.agourlay.pomf.tools.transformer;

import com.agourlay.pomf.model.Post;
import com.google.common.base.Function;

public class ExtractPostId implements Function<Post, Long>{

	@Override
	public Long apply(Post post) {
		return post.getId();
	}

}
