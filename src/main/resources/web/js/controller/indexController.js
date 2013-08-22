App.IndexController = Ember.ObjectController.extend({
	toDemo : function(){
		this.transitionToRoute('fridge', App.Dao.initSessionData("demo"));
	},

	toFeedback : function(){
		this.transitionToRoute('fridge', App.Dao.initSessionData("feedback"));
	},	

	toNewFridge : function(name){
		this.transitionToRoute('fridge', App.Dao.initSessionData(name));	
	}

});