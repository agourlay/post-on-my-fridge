App.Router.map(function() {
    this.resource('index', { path:'/'}); 
    this.resource('fridges');
    this.resource('fridge', { path:'/fridge/:fridge_id' });
    this.resource('metrics');
});

App.FridgeRoute = Ember.Route.extend({
	model: function(params) {
		return App.Dao.initSessionData(params.fridge_id);
  	},
	deactivate: function(transition) {
	    if (transition != null){
	    	this.controllerFor('messages').leaveChat(transition.params.fridge_id);
	    }
	}
});

App.FridgesRoute = Ember.Route.extend({
	model: function() {
		return App.Dao.getFridges(1);
  	}
});

App.IndexRoute = Ember.Route.extend({
	redirect: function() {
    	this.transitionTo('fridges');
  	}
});

App.MetricsRoute = Ember.Route.extend({
	model: function() {
    	return App.Dao.metrics();
  	}
});

