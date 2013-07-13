App.FridgeController = Ember.ObjectController.extend({
	toggleChat : function(){
		App.Dao.toggleChatMode();
	},

	addDefaultPost : function(){
		App.Dao.addDefaultPost();
	},

	toIndex : function(){
		this.transitionToRoute('index');
	}
});