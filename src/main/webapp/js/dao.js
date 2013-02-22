App.Dao = Em.Object.create({

	findFridgeById : function(fridgeId) {
		$.getJSON("/fridge/" + fridgeId, function(fridge) {
			console.log("Dao received for id:"+fridgeId+" - "+ fridge);
			if (fridge !== null && fridge !== undefined) {
				return App.Fridge.createWithMixins(fridge);
			}
		});
	},

	refresh : function(fridgeId) {
		alert('Refresh');
	}
});