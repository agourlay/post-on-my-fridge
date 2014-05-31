App.PostsController = Ember.ArrayController.extend({

	init : function(){
		var me = this;
		App.Dao.set("postsController", this);
		App.Dao.get("eventBus").onValue(function(evt){
			var payload = evt.payload;
			var timestamp = evt.timestamp;
            if (evt.command === "postCreated" ) {
				me.createPostOnFridge(payload);
			}
			if (evt.command === "postUpdated" ) {
				me.updateExistingPost(payload);
			}
			if (evt.command === "postDeleted") {
				me.deleteById(payload);
			}
        });
	},
	
	createPost: function(postData) {
		var controller = this;
		var model = App.Post.createWithMixins(postData);
		model.createPost().then(function(postCreated){
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
		if (post != undefined) {
			post.set('content', postInput.content);
			post.set('color', postInput.color);
			var newFullPosition = postInput.positionX + ' ' + postInput.positionY;
			if (post.get('fullPosition') !== newFullPosition){
				post.set('fullPosition', newFullPosition);
			}
		} else {
			// this post should already exist, let´s synch!
			this.resyncContent();
		}		
	},

	createPostOnFridge : function(post) {
		var exists = this.findProperty("id", post.id);
		if (exists == undefined) {
			this.pushObject(App.Post.createWithMixins(post));
		} else {
			// this post should not already exist, let´s synch!
			this.resyncContent();
		}
	},

	resyncContent : function() {
		this.transitionToRoute('fridge', App.Dao.get("fridgeId"));
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