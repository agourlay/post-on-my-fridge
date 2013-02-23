App.IndexView = Ember.View.extend({
	demo : function (){
		App.Dao.findFridgeByName("demo");
	}.property()
});