package com.agourlay.pomf.dao;

import com.agourlay.pomf.model.Fridge;
import com.agourlay.pomf.model.Post;
import com.agourlay.pomf.model.FridgeUser;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

/**
 * DAO Objectify
 *
 */
public class ObjectifyDao extends DAOBase {
        static {
                ObjectifyService.register(Post.class);
                ObjectifyService.register(Fridge.class);
                ObjectifyService.register(FridgeUser.class);
        }
}
