// 'http://localhost:8888/#/fridge/demo
App.Router.map(function() {
    this.resource('index'); 
    this.resource('fridges');
    this.resource('fridge', { path:'/fridge/:fridge_id' }, function() {
    	this.route('new');
  	});
});

App.FridgeIndexRoute = Ember.Route.extend({
   model: function(params) {
    return App.Dao.findFridgeById(params.fridge_id);
  }
});