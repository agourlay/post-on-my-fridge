App.FridgeView = Em.View.extend({
	tagName : 'div',
	classNames: ['global'],
	contentBinding: 'controller.content',
	
	rssUrl: function() {
		var fridgeName = this.get('content').get('id');
		return "/api/rss/fridge/" + fridgeName;
	}.property('content.id'),

	didInsertElement : function() {
		document.title = "Fridge "+ this.get('content').get('id');
	}
});