App.HeaderView = Ember.View.extend({
	fridgeId: function(){
		return App.get('fridgeId')
	}.property(),

	tagName: 'div',
	elementId: 'fridge-title',
	templateName: 'header-template'
});