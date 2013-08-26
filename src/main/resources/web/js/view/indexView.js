App.IndexView = Ember.View.extend({
	contentBinding: 'controller.content',
	
	newFridgeName:null,
	

	didInsertElement : function() {
		document.title = "Post on my fridge ";		
	}
});
