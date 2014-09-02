App.MessagesView = Ember.CollectionView.extend({
	tagName: 'div',
	elementId: 'chat-messages',
	itemViewClass: 'message',
	contentBinding: 'controller.content',
	
	didInsertElement: function() {
		var view = this;
		view.fitSize();
		$(window).resize(function() {
  			view.fitSize();
		});
	},

	fitSize : function() {
		this.$().height($(window).height() - 170);
	}
});