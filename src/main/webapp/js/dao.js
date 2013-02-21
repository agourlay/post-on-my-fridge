App.Dao = Em.Object.create({
	findFridgeById : function(fridgeId) {
		$.getJSON("/fridge/" + fridgeId, function(fridge) {
			if (fridge !== null && fridge !== undefined) {
				return App.Fridge.createWithMixins(fridge);
			}
		});
	}
});