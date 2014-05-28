App.FridgesController = Ember.ArrayController.extend({
	content: [],
	toNewFridge : function(name){
		this.transitionToRoute('fridge', App.Dao.initSessionData(name));	
	}
});