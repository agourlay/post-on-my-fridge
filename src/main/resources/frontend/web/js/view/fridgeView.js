App.FridgeView = Em.View.extend({
	tagName : 'div',
	elementId: 'fridge',
	contentBinding: 'controller.content',
	readMode : true,

	editMode: function(e) {
    	this.set('readMode',!this.get('readMode'));
  	},

	saveDescription: function(e) {
    	this.get('content').set("description", this.get('textFieldDescription.value'));
    	this.set('readMode',true);
    	this.get('controller').updateDescription(this.get("name"), this.get("description"));
  	},

	fridgeName: function() {
		return this.get('content.id');
	}.property('content.id'),

	rssUrl: function() {
		var fridgeName = this.get('content.id');
		document.title = "Fridge "+ fridgeName;
		return "/rss/fridge/" + fridgeName;
	}.property('content.id')
});