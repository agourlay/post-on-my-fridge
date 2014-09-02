App.PostsView = Ember.CollectionView.extend({
	tagName : 'section',
	elementId : 'fridge-content',
	itemViewClass : 'post',
	contentBinding: 'controller.content',
	
	didInsertElement : function() {
		this.$().droppable({accept: ".post"});
		this.$().height($(window).height() * 2);
		konami();
	}
});