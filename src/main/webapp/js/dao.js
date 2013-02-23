App.Dao = Em.Object.create({

	fridgeId : null,
	posts : [],

	findFridgeByName : function(fridgeId) {
		var fridgeRetrieved = null; 
		console.log("Dao requesting fridge :"+fridgeId);
		$.ajax({
	        async: false,   // forces synchronous call
	        url: "api/fridge/" + fridgeId,
	        type: 'GET',
	        success: function(fridge) {
				if (fridge !== null && fridge !== undefined) {
					console.log("Dao received proper fridge for id :" + fridgeId + " - " + JSON.stringify(fridge));
					fridge.posts = _.map(fridge.posts, function(post){ return App.Post.createWithMixins(post); });
					fridgeRetrieved = App.Fridge.createWithMixins(fridge);
				}
	        }
    	});
		this.set('fridgeId',fridgeId);
		this.set('posts',fridgeRetrieved.get('posts'));
		return fridgeRetrieved;
	},

	refresh : function() {
		this.findFridgeByName(this.get('fridgeId'));
	}
});