App.MetricsView = Em.View.extend({
    tagName : 'div',
    elementId: 'metrics',
    contentBinding: 'controller.content'      
});

Handlebars.registerHelper('toFixed', function(number, digits) {
  return Ember.get(this,number).toFixed(digits);
});