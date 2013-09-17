App.IndexView = Ember.View.extend({
	contentBinding: 'controller.content',
	tagName : 'div',
    elementId: 'indexView',
	newFridgeName:null,
	

	didInsertElement : function() {
		document.title = "Post on my fridge ";		
	}
});
