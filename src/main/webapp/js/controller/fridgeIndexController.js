App.FridgeIndexController = Ember.ObjectController.extend({
	content: null,

	watchContent: function() {
		alert('FridgeIndexController content changed : '+ this.get('content'));
	}.observes('content')

});