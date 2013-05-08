App.MessagesView = Ember.CollectionView.extend({
	tagName: 'div',
	classNames: ['chatLog'],
	itemViewClass: 'App.MessageView',
	contentBinding: 'controller.content'
});