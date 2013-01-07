App.HeaderView = Ember.View.extend({
	tagName: 'div',
	elementId: 'fridge-title',
	templateName: 'header-template',
	fridgeId: function() {
		return App.get('fridgeId')
	}.property(),
	rssUrl: function() {
		return "/fridge/" + this.get('fridgeId') + "/rss";
	}.property()
});