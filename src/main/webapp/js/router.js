// 'http://localhost:8888/#/fridge/demo
App.Router.map(function() {
    this.resource('index'); 
    this.resource('fridges');
    this.resource('fridge', { path:'/fridge/:fridge_name' });
});

App.FridgeRoute = Ember.Route.extend({
   model: function(params) {
    return App.Dao.findFridgeByName(params.fridge_name);
  }
});