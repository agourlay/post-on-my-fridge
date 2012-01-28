package com.agourlay.pomf.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agourlay.pomf.dao.Dao;
import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.tools.rss.Feed;
import com.agourlay.pomf.tools.rss.FeedMessage;
import com.agourlay.pomf.tools.rss.RSSFeedWriter;
import com.agourlay.pomf.tools.rss.RssUtils;

public class GetRssFeedController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/xml");
		Feed rssFeeder = createRssFeed();
		rssFeeder.getMessages().addAll(getRssEntry());
		RSSFeedWriter writer = new RSSFeedWriter(rssFeeder, new ByteArrayOutputStream());
		PrintWriter printWriter = resp.getWriter();
		try {
			String content = writer.write().toString();
			printWriter.print(content);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Feed createRssFeed() {
		String copyright = "Nya";
		String title = "Post On My Fridge";
		String description = "Content of your fridge";
		String language = "en";
		String link = "http://post-on-my-fridge.appspot.com/";
		Calendar cal = new GregorianCalendar();
		Date creationDate = cal.getTime();
		SimpleDateFormat date_format = new SimpleDateFormat(
				"EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
		String pubdate = date_format.format(creationDate);
		Feed rssFeeder = new Feed(title, link, description, language,
				copyright, pubdate);
		return rssFeeder;
	}

	public List<FeedMessage> getRssEntry() {
		List<FeedMessage> listFeed = new ArrayList<FeedMessage>();
		List<Post> posts = Dao.INSTANCE.getPosts();
		for (Post post : posts) {
			listFeed.add(RssUtils.createFeedMessageFromPost(post));
		}
		return listFeed;
	}
}
