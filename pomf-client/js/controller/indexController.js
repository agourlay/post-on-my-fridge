App.IndexController = Ember.ObjectController.extend({
	toDemo : function(){
		this.transitionToRoute('fridge', App.Dao.initSessionData("demo"));
	},

	toNewFridge : function(name){
		this.transitionToRoute('fridge', App.Dao.initSessionData(name));	
	}

});