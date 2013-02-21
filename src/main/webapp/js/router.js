// '/#/fridges
App.Router.map(function() {
    this.resource('index'); 
    this.resource('fridges');
    this.resource('fridge', { path:'/fridge/:fridge_id' }, function() {
    	this.route('new');
  	});
});