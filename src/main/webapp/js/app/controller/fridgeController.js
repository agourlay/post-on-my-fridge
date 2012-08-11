App.FridgeController = Ember.ArrayController.create({
		content: [],
		fridgeId : $("#fridgeId").val(),

		init : function (){
			this.retrievePost();
			return this._super();
		},

		createPost : function (postData){
			App.Post.create(postData).createPost();
		},

		deletePost : function (id){
			postToRemove = this.findProperty('id', id);
			this.removeObject(postToRemove);
			postToRemove.deletePost();
		},

		updateExistingPost : function(postInput){
			post = this.findProperty('id', postInput.id);
			post.set('positionX', postInput.positionX);
			post.set('positionY', postInput.positionY);
		},

		addPost : function(post) {
			post.textColor = getTxtColorFromBg(post.color);
			post.relativeDate = moment(post.date).fromNow();
			post.content = generatePostContent(post);
			this.pushObject(App.Post.create(post));
  		},

		updateExistingPost : function(postInput){
			post = this.findProperty('id', postInput.id);
			post.set('positionX', postInput.positionX);
			post.set('positionY', postInput.positionY);
		},

  		createOrUpdate : function (post){
  			var exists = this.filterProperty('id', post.id).length;
  			if (exists === 0) {
				this.addPost(post);
			}else{
				this.updateExistingPost(post);
			}
		},

		deleteProcedure : function(fridgeContent){
			var me = this;
			$.each(me.get('content'),function(indexPost,valuePost){
				postId = valuePost.id;
				exist = _.find(fridgeContent, function(dataPost){
						return dataPost.id == postId;
					});
				if (!exist){
					me.removeObject(me.findProperty('id', postId));
				}
			});
		},

  		retrievePost : function(){
  			var me = this;
			$.getJSON("/resources/fridge/" + this.fridgeId , function(fridgeContent) {
				if (fridgeContent !== undefined){
					// remove post present in the fridge but not in the db
					me.deleteProcedure(fridgeContent);
					
					//update or create the posts
					$.each(fridgeContent, function(index,post){
						me.createOrUpdate(post);
					});	
				}	
			});
  		}
	});