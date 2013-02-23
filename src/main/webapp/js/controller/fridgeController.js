App.FridgeController = Ember.ObjectController.extend({
	watchContent: function() {
		console.log("FridgeContent content changed :" + JSON.stringify(this.get('content')));
	}.observes('content')
});