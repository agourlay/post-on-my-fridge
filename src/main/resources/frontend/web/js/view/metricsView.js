App.MetricsView = Em.View.extend({
    tagName : 'div',
    elementId: 'metrics',
    contentBinding: 'controller.content',

    didInsertElement: function() {
		document.title = "Metrics | Post on my fridge";
	}	      
});

Handlebars.registerHelper('toFixed', function(number, digits) {
  return Ember.get(this,number).toFixed(digits);
});