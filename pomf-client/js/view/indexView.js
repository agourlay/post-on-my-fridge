App.IndexView = Ember.View.extend({
	didInsertElement : function() {	
		var view = this;

		$("#button-newfridge").click(function(e) {
		   e.preventDefault();
		   var target = $("#newFridgeValue").val();
		   if (target !== undefined && target !== ""){
		   	  view.get('controller').toNewFridge(target);
		   }
		});
	}
});