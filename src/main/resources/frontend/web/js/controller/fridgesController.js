App.FridgesController = Ember.ArrayController.extend({
	toNewFridge : function(name){
		this.transitionToRoute('fridge', App.Dao.initSessionData(name));	
	}
});