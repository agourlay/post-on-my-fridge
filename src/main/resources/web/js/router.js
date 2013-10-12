App.Router.map(function() {
    this.resource('index', { path:'/'}); 
    this.resource('fridges');
    this.resource('trends');
    this.resource('fridge', { path:'/fridge/:fridge_id' });
});

App.FridgeRoute = Ember.Route.extend({
	model: function(params) {
		return App.Dao.initSessionData(params.fridge_id);
  	},
	beforeModel: function(transition) {
	    if (App.Dao.get('fridgeId') != null){
	    	this.controllerFor('messages').leaveChat(transition.params.fridge_id);
	    }
	}
});

App.FridgesRoute = Ember.Route.extend({
	model: function() {
		return App.Dao.getFridges();
  	}
});

App.IndexRoute = Ember.Route.extend({
	model: function() {
		return App.Dao.getStats();
  	}
});

