App.FridgeController = Ember.ObjectController.extend({
	watchContent: function() {
		console.log("FridgeController content changed :" + JSON.stringify(this.get('content')));
	}.observes('content')
});