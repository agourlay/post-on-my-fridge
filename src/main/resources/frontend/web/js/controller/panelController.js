App.PanelController = Ember.ObjectController.extend({
	actions: {  
		addDefaultPost : function(){
			App.Dao.addPostAtCoordinate(getRandomPostInitX(), getRandomPostInitY());
		}
	}
});