App.PostsView = Ember.CollectionView.extend({
	tagName : 'section',
	elementId : 'fridge-content',
	itemViewClass : 'App.PostView',
	contentBinding: 'controller.content',
	
	didInsertElement : function() {
		this.$().droppable({accept: ".post"});
		konami();
	}
});