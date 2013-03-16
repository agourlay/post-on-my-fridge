App.Dao = Em.Object.create({

	fridgeId : null,
	posts : [],

	initApp : function(chatController,postController) {
		chatController.initController();
		this.streamManagement(chatController);
	},

	streamManagement : function () {
		var me = this;
		var source = new EventSource("stream/" + this.get('fridgeId'));
		source.addEventListener('message', function(e) {
			console.log(e.data);
			var data = $.parseJSON(e.data);
			if (data.command === "refresh") {
				me.refresh();
			}
			if (data.command === "message") {
				chatController.messageManagement(data.user, data.message,data.timestamp);
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
					console.log("Dao received proper fridge for id :" + fridgeId + " - " + JSON.stringify(fridge));
					fridge.posts = fridge.posts.map(function(post){ return App.Post.createWithMixins(post); });
					fridgeRetrieved = App.Fridge.createWithMixins(fridge);
				}
	        }
    	});
		return fridgeRetrieved;
	},

	refresh : function() {
		this.set('posts',this.findFridgeByName(this.get('fridgeId')).get('posts'));
	}
});