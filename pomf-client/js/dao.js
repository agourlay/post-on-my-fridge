App.Dao = Em.Object.create({

	fridgeId : null,
	source : null,
	chatController : null,
	postsController : null,
	userToken: null,

	initApp : function(chatController) {
		chatController.initController();
		this.set('chatController',chatController);
		this.retrieveUserToken();
	},

	addDefaultPost : function (){
		var newPostData = {};
		newPostData.author = App.Dao.pseudo();
		newPostData.content = "New post -> edit me with a double click!";
		newPostData.color = "#f7f083";
		newPostData.positionX = 0.5;
		newPostData.positionY = 0.04;
		newPostData.fridgeId = App.Dao.get('fridgeId');
		newPostData.dueDate = "";
		newPostValidation(newPostData);
		this.get('postsController').createPost(newPostData);
	},

	streamManagement : function (postsController) {
		var me = this;
		var chatController = this.get('chatController');
		this.set("postsController",postsController);
		me.set("source", new EventSource("stream/" + this.get('fridgeId') +"/"+ this.get("userToken")));
		var source = me.get("source");
		source.addEventListener('message', function(e) {
			var data = $.parseJSON(e.data);
			console.dir(data);
			var payload = $.parseJSON(data.payload);
			if (data.command === "update" || data.command === "create" ) {
				postsController.createOrUpdate(payload);
			}
			if (data.command === "delete") {
				postsController.deleteById(payload);
			}
			if (data.command === "message") {
				chatController.messageManagement(payload);
			}
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
		if ($("#pseudo").val() !== ""){
			var inputName = $("#pseudo").val();
			store.set('username', inputName);
			return inputName;
		}		
		return "Anonymous";	
	},

	findFridgeByName : function(fridgeId) {
		this.set('fridgeId',fridgeId);
		console.log("Dao requesting fridge :"+fridgeId);
		var model = App.Fridge.create();
		$.ajax({
	        url: "api/fridge/" + fridgeId,
	        type: 'GET',
	        success: function(fridge) {
				if (fridge !== null && fridge !== undefined) {
					model.set('id', fridge.name);
					model.set('description', fridge.description);
					model.set('posts', fridge.posts.map(function(post){ return App.Post.createWithMixins(post); }));
					model.set('loaded', true);
				}
	        }
    	});
		return model;
	},

	retrieveUserToken : function() {
		$.ajax({
	        url: "api/token/",
	        type: 'GET',
	        success: function(token) {
				if (token !== null && token !== undefined) {
					App.Dao.set('userToken',token);
				}
	        }
    	});
	}
});