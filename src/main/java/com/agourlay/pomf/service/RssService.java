package com.agourlay.pomf.service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.rss.Feed;
import com.agourlay.pomf.rss.FeedMessage;
import com.agourlay.pomf.rss.RSSFeedWriter;
import com.agourlay.pomf.util.Constantes;

public class RssService {

	public static String getRssStream(List<Fridge> fridges,String feedName) throws Exception{
		Feed rssFeeder = RssService.createRssFeed(feedName);
		rssFeeder.getMessages().addAll(RssService.getRssEntry(fridges));
		RSSFeedWriter writer = new RSSFeedWriter(rssFeeder, new ByteArrayOutputStream());
		return writer.write().toString();
	}
	
	public static FeedMessage createFeedMessageFromPost(Post post, String fridgeId) {
		FeedMessage feed = new FeedMessage();
		feed.setTitle(post.getAuthor() + " posted on your fridge");
		feed.setDescription(post.getContent());
		feed.setAuthor(post.getAuthor());
		feed.setGuid(Constantes.FRIDGE_URL + fridgeId + "#" + post.getId());
		feed.setLink(Constantes.FRIDGE_URL);
		return feed;
	}

	public static Feed createRssFeed(String fridgeId) {
		String copyright = "meh?";
		String title = "Post On My Fridge : " + fridgeId;
		String description = "Content of the fridge " + fridgeId;
		String language = "en";
		String link = Constantes.FRIDGE_URL + "/fridge/" + fridgeId;
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
				listFeed.add(RssService.createFeedMessageFromPost(post, fridge.getName()));
			}
		}
		return listFeed;
	}
}
