App.IndexView = Ember.View.extend({
	didInsertElement : function() {	
		$('#myCarousel').carousel({
  			interval: 3000
		})
	}
});