package com.agourlay.pomf.conf;

import javax.servlet.http.HttpServlet;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.FridgeUser;
import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.model.Stat;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.impl.translate.opt.joda.JodaTimeTranslators;

public class Config extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static {
		JodaTimeTranslators.add(ObjectifyService.factory());
		ObjectifyService.register(Post.class);
		ObjectifyService.register(Fridge.class);
		ObjectifyService.register(FridgeUser.class);
		ObjectifyService.register(Stat.class);
	}
	
}
