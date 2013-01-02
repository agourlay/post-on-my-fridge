App.FridgeView = Ember.CollectionView.extend({
		contentBinding: 'App.FridgeController',
		tagName: 'section',
		elementId: 'fridge',

		didInsertElement: function() {
			initUIElement();
		},

		itemViewClass: 'App.PostView'
});