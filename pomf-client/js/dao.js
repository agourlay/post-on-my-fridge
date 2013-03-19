App.Dao = Em.Object.create({

	fridgeId : null,
	source : null,
	chatController : null,

	initApp : function(chatController) {
		chatController.initController();
		this.set('chatController',chatController);
	},

	streamManagement : function (postsController) {
		var me = this;
		var chatController = this.get('chatController');
		me.set("source",new EventSource("stream/" + this.get('fridgeId')));
		var source = me.get("source");
		source.addEventListener('message', function(e) {
			var data = $.parseJSON(e.data);
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
	}
});