App.FridgeView = Ember.CollectionView.extend({
		contentBinding: 'App.FridgeController',
		tagName: 'section',
		elementId: 'fridge',
		itemViewClass: 'App.PostView',
		didInsertElement: function() {
			initUIElement();
		}
});