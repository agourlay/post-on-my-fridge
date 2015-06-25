App.Router.map(function() {
  this.resource('index', { path:'/'}); 
  this.resource('fridges');
  this.resource('fridge', { path:'/fridge/:fridge_id' });
});

App.FridgeRoute = Ember.Route.extend({
	model: function(params) {
	  return App.Dao.initSessionData(params.fridge_id);
  },

  setupController: function(controller, model) {
    controller.set('model', model);
    this.controllerFor('messages').reload();
  },

  actions: {
    willTransition: function(transition) {
      App.Dao.leaveChatOnExit();
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