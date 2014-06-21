App.PanelController = Ember.ObjectController.extend({
	actions: {  
		addDefaultPost : function(){
			App.Dao.addDefaultPost();
		},
	}
});