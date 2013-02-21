App.Fridge = Em.Object.create({
	
	find : function(fridgeId) {
		$.getJSON("/fridge/" + this.fridgeId, function(fridgeContent) {
			if (fridgeContent !== null && fridgeContent !== undefined && fridgeContent.posts !== null) {
				return fridgeContent.posts;
			}
		});
	}
});