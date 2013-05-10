App.ChatView = Ember.View.extend({
	tagName: 'aside',
	classNames: ['chatPanel'],
	contentBinding: 'controller.content',

	didInsertElement : function() {	
		if(typeof store.get('username') !== "undefined"){
			$("#pseudo").val(store.get('username'));
		}
	}
});