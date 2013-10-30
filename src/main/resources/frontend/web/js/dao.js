App.Dao = Em.Object.create({

	fridgeId : null,
	source : null,
	messagesController : null,
	postsController : null,
	userToken: null,
	eventBus:null,

	initSessionData : function(fridgeId) {
		var me = this;
		this.set('fridgeId',fridgeId);
		this.set('eventBus', new Bacon.Bus())
		if(this.get("userToken") == null){
				me.retrieveUserToken().done(function(){
					me.streamManagement();
			});
		}
		if (this.get("messagesController") != null){
			this.get("messagesController").reload();
		}
		return this.findFridgeByName(fridgeId);
	},

	addDefaultPost : function (){
		var newPostData = {};
		newPostData.author = App.Dao.pseudo();
		newPostData.content = "?";
		newPostData.color = randomColor();
		newPostData.positionX = getRandomPostInitX();
		newPostData.positionY = getRandomPostInitY();
		newPostData.fridgeId = App.Dao.get('fridgeId');
		newPostData.dueDate = "";
		newPostValidation(newPostData);
		this.get('postsController').createPost(newPostData);
	},

	addLocalMessage : function (message) {
		debugger;
		this.get('messagesController').messageManagement(message);
	},

	streamManagement : function () {
		var me = this;
		me.set("source", new EventSource("stream/fridge/" + this.get('fridgeId') +"?token="+ this.get("userToken")));
		var source = me.get("source");
		source.addEventListener('message', function(e) {
			var data = $.parseJSON(e.data);
			var payload = data.payload;
			var timestamp = data.timestamp;
			me.get("eventBus").push(data);
		}, false);

		source.addEventListener('open', function(e) {
			console.log("SSE opened!")
		}, false);

		source.addEventListener('error', function(e) {
			if (e.readyState == EventSource.CLOSED) {
			    errorMessage("Streaming service error");
			}
		}, false);
	},

	pseudo : function() {
		if(typeof store.get('username') == "undefined"){
			store.set('username', "Anonymous");
		}
		return store.get('username');	
	},

	leaveChatOnExit : function () {
		if (this.get('fridgeId') != null) {
		    this.get("messagesController").leaveChat(this.get('fridgeId'));
		}
	},

	renameParticipant : function (newName) {
		this.get("messagesController").renameParticipant(newName);
	},

	findFridgeByName : function(fridgeId) {
		var model = App.Fridge.create();
		model.set('id', fridgeId);
		$.ajax({
        	url: "fridges/" + fridgeId,
        	type: 'GET',
        	beforeSend : function (){
        		NProgress.start(); 
            },
        	success: function(fridge) {
				if (fridge !== null && fridge !== undefined) {
					model.set('name', fridge.name);
					model.set('creationDate', fridge.creationDate);
					model.set('modificationDate', fridge.modificationDate);
					model.set('posts', fridge.posts.map(function(post){ return App.Post.createWithMixins(post); }));
					model.set('loaded', true);
				}
				NProgress.done();
        	},
        	error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during fridge retrieval");
			}
    	});
		return model;
	},

	retrieveUserToken : function() {
		return $.ajax({
	        url: "token",
	        type: 'GET',
	        success: function(token) {
				if (token !== null && token !== undefined) {
					App.Dao.set('userToken',token);
				}
	        },
	        error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during token retrieval");
			}
    	});
	},

	getStats : function () {
		var idxModel = App.Index.create();
		var fridgeCountDefer = $.ajax({
	        	url: "count/fridges",
	        	type: 'GET',
	        	success: function(nbfridge) {
					if (nbfridge !== null && nbfridge !== undefined) {
						idxModel.set('nbFridges', nbfridge);
					}
	        	},
	        	error: function(xhr, ajaxOptions, thrownError) {
					errorMessage("Error during count fridges retrieval");
			}
    	});

		var postCountDefer = $.ajax({
        	url: "count/posts",
        	type: 'GET',
        	success: function(nbpost) {
				if (nbpost !== null && nbpost !== undefined) {
					idxModel.set('nbPosts', nbpost);
				}
        	},
        	error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during count posts retrieval");
			}
		});

		return idxModel;
	},

	getFridges : function () {
		var fridgesModel = Ember.A([]);
		$.ajax({
	        	url: "fridges/",
	        	type: 'GET',
	        	success: function(fridges) {
					$.each( fridges, function(i, fridge){
						var fridge = App.Fridge.create({
							id:fridge.id,
							name:fridge.name,
							creationDate:fridge.creationDate,
							modificationDate:fridge.modificationDate,
							posts: fridge.posts.map(function(post){ return App.Post.createWithMixins(post); })
						});
						fridgesModel.pushObject(fridge);	
					});					
	        	},
	        	error: function(xhr, ajaxOptions, thrownError) {
					errorMessage("Error during fridges retrieval");					
				}
    		});
		return fridgesModel;
	}
});

