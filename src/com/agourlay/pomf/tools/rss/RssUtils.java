package com.agourlay.pomf.tools.rss;

import com.agourlay.pomf.model.Post;

public class RssUtils {

	public static FeedMessage createFeedMessageFromPost(Post post) {
		FeedMessage feed = new FeedMessage();
		feed.setTitle(post.getAuthor()+" posted on your fridge");
		feed.setDescription(post.getContent());
		feed.setAuthor(post.getAuthor());
		feed.setGuid("http://post-on-my-fridge.appspot.com/"+post.getId());
		feed.setLink("http://post-on-my-fridge.appspot.com/");
		return feed;
	}
}
