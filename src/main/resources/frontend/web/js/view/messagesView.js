App.MessagesView = Ember.CollectionView.extend({
	tagName: 'div',
	elementId: 'chat-messages',
	itemViewClass: 'App.MessageView',
	contentBinding: 'controller.content'
});