App.FridgeView = Em.View.extend({
	tagName : 'div',
	elementId: 'fridge',
	contentBinding: 'controller.content',

	didInsertElement: function() {
		document.title = this.get('content.name') + " | Post on my fridge";
	},

	doubleClick: function(event) {
		var fridge = $('#fridge-content');
		var x = (event.clientX - 100) / fridge.width();
		var y = (event.clientY - 100) / fridge.height();
		App.Dao.addPostAtCoordinate(x, y);
		event.stopPropagation();
	}	
});