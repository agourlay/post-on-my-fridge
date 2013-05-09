App.ChatView = Ember.View.extend({
	tagName: 'aside',
	classNames: ['chatPanel'],
	contentBinding: 'controller.content'
});