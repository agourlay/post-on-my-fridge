//Init ember.js
App = Em.Application.create({

	ready : function(){
			initUIElement();
			setTimeout(showFridge, 1000);
	}
	
});