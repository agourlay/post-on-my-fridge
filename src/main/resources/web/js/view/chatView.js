App.ChatView = Ember.View.extend({
	tagName: 'aside',
	classNames: ['chatPanel'],
	contentBinding: 'controller.content',

	didInsertElement : function() {	
		// pseudo management
		if(typeof store.get('username') !== "undefined"){
			$("#pseudo").val(store.get('username'));
		}
		// panel management
		if(App.Dao.get('chatMode')){
			this.$().animate({width:'toggle'},300);
		}
	}
});