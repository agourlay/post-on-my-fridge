App.IndexView = Ember.View.extend({
	didInsertElement : function() {	
		var view = this;
		$('#myCarousel').carousel({
  			interval: 3000
		});

		$("#button-newfridge").click(function(e) {
		   e.preventDefault();
		   var target = $("#newFridgeValue").val();
		   if (target !== undefined && target !== ""){
		   	  view.get('controller').toNewFridge(target);
		   }
		});
	}
});