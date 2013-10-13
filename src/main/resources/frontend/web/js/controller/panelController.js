App.PanelController = Ember.ObjectController.extend({

	addDefaultPost : function(){
		App.Dao.addDefaultPost();
	},

	toIndex : function(){
		this.transitionToRoute('index');
	}
});