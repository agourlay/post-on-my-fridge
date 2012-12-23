package com.agourlay.pomf.rss;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.Post;

public class RssUtils {

	public static FeedMessage createFeedMessageFromPost(Post post, String fridgeId) {
		FeedMessage feed = new FeedMessage();
		feed.setTitle(post.getAuthor() + " posted on your fridge");
		feed.setDescription(post.getContent());
		feed.setAuthor(post.getAuthor());
		feed.setGuid("http://post-on-my-fridge.appspot.com/" + fridgeId + "#" + post.getId());
		feed.setLink("http://post-on-my-fridge.appspot.com/");
		return feed;
	}

	public static Feed createRssFeed(String fridgeId) {
		String copyright = "Nya";
		String title = "Post On My Fridge : " + fridgeId;
		String description = "Content of the fridge " + fridgeId;
		String language = "en";
		String link = "http://post-on-my-fridge.appspot.com/fridge/" + fridgeId;
		Date creationDate = new Date();
		SimpleDateFormat date_format = new SimpleDateFormat("EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
		String pubDate = date_format.format(creationDate);
		Feed rssFeeder = new Feed(title, link, description, language, copyright, pubDate);
		return rssFeeder;
	}

	public static List<FeedMessage> getRssEntry(List<Fridge> fridges) {
		List<FeedMessage> listFeed = new ArrayList<FeedMessage>();
		for (Fridge fridge : fridges) {
			for (Post post : fridge.getPosts()) {
				listFeed.add(RssUtils.createFeedMessageFromPost(post, fridge.getName()));
			}
		}
		return listFeed;
	}
}
