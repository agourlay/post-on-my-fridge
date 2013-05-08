App.ChatView = Ember.View.extend({
	tagName: 'div',
	classNames: ['chatPanel'],
	contentBinding: 'controller.content'
});