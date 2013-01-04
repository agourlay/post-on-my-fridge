App.HeaderView = Ember.View.extend({
	fridgeId: function() {
		return App.get('fridgeId')
	}.property(),

	rssUrl: function() {
		return "/fridge/" + this.get('fridgeId') + "/rss";
	}.property(),

	tagName: 'div',
	elementId: 'fridge-title',
	templateName: 'header-template'
});