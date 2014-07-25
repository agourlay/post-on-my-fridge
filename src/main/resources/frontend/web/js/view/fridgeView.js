App.FridgeView = Em.View.extend({
	tagName : 'div',
	elementId: 'fridge',
	contentBinding: 'controller.content',

	didInsertElement: function() {
		document.title = this.get('content.name') + " | Post on my fridge";
	}	
});