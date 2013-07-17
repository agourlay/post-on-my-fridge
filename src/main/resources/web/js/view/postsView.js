App.PostsView = Ember.CollectionView.extend({
	tagName : 'section',
	elementId : 'fridge',
	itemViewClass : 'App.PostView',
	contentBinding: 'controller.content',
	
	didInsertElement : function() {
		var view = this;
		view.$().droppable({accept: ".post"});
		konami();
	}
});