App.Dao = Em.Object.create({

	fridgeId : null,
	posts : [],

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