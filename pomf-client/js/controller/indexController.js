App.IndexController = Ember.ObjectController.extend({
	toDemo : function(){
		this.transitionToRoute('fridge', App.Dao.initSessionData("demo"));
	}
});