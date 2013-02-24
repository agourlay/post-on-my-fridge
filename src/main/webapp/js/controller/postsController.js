App.PostsController = Ember.ArrayController.extend({

	watchContent: function() {
		console.log("PostsController content changed :" + JSON.stringify(this.get('content')));
	}.observes('content'),
	
	createPost: function(postData) {
		App.Post.createWithMixins(postData).createPost();
	},

	deletePost: function(id) {
		postToRemove = this.findProperty('id', id);
		this.removeObject(postToRemove);
		postToRemove.deletePost();
	},

	updateExistingPost: function(postInput) {
		var post = this.findProperty('id', postInput.id);
		post.set('content', postInput.content);
		post.set('color', postInput.color);
		var newFullPosition = postInput.positionX + ' ' + postInput.positionY;
		if (post.get('fullPosition') !== newFullPosition){
			post.set('fullPosition', newFullPosition);
		}
	},

	createOrUpdate: function(post) {
		var exists = this.filterProperty('id', post.id).length;
		if (exists === 0) {
			this.pushObject(App.Post.createWithMixins(post));
		} else {
			this.updateExistingPost(post);
		}
	},

	deleteProcedure: function(posts) {
		var me = this;
			postsTodelete = [],
		me.forEach(function(valuePost) {
			var postId = valuePost.id;
			if (postId !== undefined && posts.findProperty('id', postId) === undefined) {
				postsTodelete.pushObject(me.findProperty('id', postId));
			}
		});
		postsTodelete.forEach(function(postToDelete) {
			infoMessage("Post from " + postToDelete.get('author') + " deleted");
			me.removeObject(postToDelete);
		});
	},

	mergePost: function() {
		var me = this,
		    posts = App.Dao.get('posts');
		if(posts !== null) {
			// remove post present in the fridge but not in the db
			me.deleteProcedure(posts);
			// update or create the posts
			posts.forEach(function(post) { me.createOrUpdate(post); });
		}
	}.observes('App.Dao.posts')
});