App.IndexController = Ember.ObjectController.extend({
	toNewFridge : function(name){
		this.transitionToRoute('fridge', App.Dao.initSessionData(name));	
	}
});