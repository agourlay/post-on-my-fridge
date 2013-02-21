window.App = Ember.Application.createWithMixins({

	// TODO remove this ugly thing
	fridgeId : "demo",

	LOG_TRANSITIONS: true,
	LOG_BINDINGS : true,
	
	ready : function() {
		document.title = "Fridge "+ this.fridgeId;
	},
	
	ApplicationView : Ember.View.extend({
		fridgeId: function() {
			return App.get('fridgeId')
		}.property(),

		rssUrl: function() {
			return "/fridge/" + this.get('fridgeId') + "/rss";
		}.property()
	})
});