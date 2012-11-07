App.FridgeView = Ember.CollectionView.extend({
		contentBinding: 'App.FridgeController',
		tagName: 'section',
		elementId: 'fridge',

		didInsertElement: function() {
			initUIElement();
		    setTimeout(showFridge, 1000); 
		},

		itemViewClass: 'App.PostView'
});