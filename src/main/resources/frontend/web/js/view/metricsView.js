App.MetricsView = Em.View.extend({
    tagName : 'div',
    elementId: 'metrics',
    contentBinding: 'controller.content',

    didInsertElement: function() {
		document.title = "Metrics | Post on my fridge";
	}	      
});

Handlebars.registerHelper('duration', function(number, digits) {
	var value = Ember.get(this,number);
	if (value > 1000) {
		return (value / 1000).toFixed(digits) + " s";
	} else {
		return value.toFixed(digits) + " ms";
	}	
});

Handlebars.registerHelper('toFixed', function(number, digits) {
  return Ember.get(this,number).toFixed(digits);
});