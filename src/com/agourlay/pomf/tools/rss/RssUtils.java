package com.agourlay.pomf.tools.rss;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.Post;

public class RssUtils {

	public static FeedMessage createFeedMessageFromPost(Post post) {
		FeedMessage feed = new FeedMessage();
		feed.setTitle(post.getAuthor()+" posted on your fridge");
		feed.setDescription(post.getContent());
		feed.setAuthor(post.getAuthor());
		feed.setGuid("http://post-on-my-fridge.appspot.com/"+post.getFridgeId()+"#"+post.getId());
		feed.setLink("http://post-on-my-fridge.appspot.com/");
		return feed;
	}
    
    public static Feed createRssFeed(String fridgeId) {
		String copyright = "Nya";
		String title = "Post On My Fridge : "+fridgeId;
		String description = "Content of the fridge "+fridgeId;
		String language = "en";
		String link = "http://post-on-my-fridge.appspot.com/"+fridgeId;
		Date creationDate = new Date();
		SimpleDateFormat date_format = new SimpleDateFormat(
				"EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
		String pubdate = date_format.format(creationDate);
		Feed rssFeeder = new Feed(title, link, description, language,
				copyright, pubdate);
		return rssFeeder;
	}

	public static List<FeedMessage> getRssEntry(String fridgeId) {
		List<FeedMessage> listFeed = new ArrayList<FeedMessage>();
		List<Post> posts = Fridge.getPosts(fridgeId);
		for (Post post : posts) {
			listFeed.add(RssUtils.createFeedMessageFromPost(post));
		}
		return listFeed;
	}
}
