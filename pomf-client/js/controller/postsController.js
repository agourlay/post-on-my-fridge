App.PostsController = Ember.ArrayController.extend({

	init : function(){
		App.Dao.streamRegistering(this,null);
	},
	
	createPost: function(postData) {
		var controller = this;
		var model = App.Post.createWithMixins(postData);
		var promiseCreation = model.createPost();
		promiseCreation.done(function(postCreated){
			model.set('id',$.parseJSON(postCreated).id);
			controller.pushObject(model);
		});
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

	deleteById : function(id) {
		var postToDelete = this.findProperty('id', id);
		if (postToDelete !== undefined){
			console.log('Delete '+ id );
			infoMessage("Post from " + postToDelete.get('author') + " deleted");
			this.removeObject(postToDelete);
		}
	}
});