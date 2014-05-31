App.FridgesController = Ember.ArrayController.extend({
	createFridge : function(name){
		var me = this;
		App.Dao.createFridge(name).then(function (fridge) {
			me.transitionToRoute('fridge', App.Dao.initSessionData(fridge.id));	
		});	
	}
});