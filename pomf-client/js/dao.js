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
			if (data.command === "update" || data.command === "create" ) {
				postsController.createOrUpdate($.parseJSON(data.payload));
			}
			if (data.command === "delete") {
				postsController.deleteById($.parseJSON(data.payload));
			}
			if (data.command === "message") {
				chatController.messageManagement($.parseJSON(data.payload));
			}
		}, false);

		source.addEventListener('open', function(e) {
			console.log("SSE opened!")
		}, false);

		source.addEventListener('error', function(e) {
			if (e.readyState == EventSource.CLOSED) {
			    errorMessage("Channel error");
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
		var fridgeRetrieved = null; 
		this.set('fridgeId',fridgeId);
		console.log("Dao requesting fridge :"+fridgeId);
		$.ajax({
	        async: false,   // forces synchronous call
	        url: "api/fridge/" + fridgeId,
	        type: 'GET',
	        success: function(fridge) {
				if (fridge !== null && fridge !== undefined) {
					console.dir(fridge);
					fridge.posts = fridge.posts.map(function(post){ return App.Post.createWithMixins(post); });
					fridgeRetrieved = App.Fridge.createWithMixins(fridge);
				}
	        }
    	});
		return fridgeRetrieved;
	}
});