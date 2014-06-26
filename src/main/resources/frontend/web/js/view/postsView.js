App.PostsView = Ember.CollectionView.extend({
	tagName : 'section',
	elementId : 'fridge-content',
	itemViewClass : 'App.PostView',
	contentBinding: 'controller.content',
	
	didInsertElement : function() {
		this.$().droppable({accept: ".post"});
		this.$().height($(window).height() * 2);
		konami();
	}
});