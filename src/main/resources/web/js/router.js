App.Router.map(function() {
    this.resource('index', { path:'/'}); 
    this.resource('fridges');
    this.resource('trends');
    this.resource('fridge', { path:'/fridge/:fridge_id' });
});

App.FridgeRoute = Ember.Route.extend({
	model: function(params) {
		var fridgeName = params.fridge_id;
		return App.Dao.initSessionData(fridgeName);
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

