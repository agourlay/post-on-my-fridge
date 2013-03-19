App.PostsController = Ember.ArrayController.extend({

/*	watchContent: function() {
		console.log("PostsController content changed :" + JSON.stringify(this.get('content')));
	}.observes('content'),*/
	
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
			console.dir(App.Post.createWithMixins(post));
			this.pushObject(App.Post.createWithMixins(post));
		} else {
			this.updateExistingPost(post);
		}
	},

	deleteById : function(id) {
		var postToDelete = this.findProperty('id', id);
		if (postToDelete !== undefined){
			console.log('Delete '+ id );
			infoMessage("Post from " + postToDelete.get('author') + " deleted");
			this.removeObject(postToDelete);
		}
	}
});