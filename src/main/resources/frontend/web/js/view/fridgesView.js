App.FridgesView = Em.View.extend({
	tagName : 'div',
    elementId: 'fridgesView',
	newName: null,

	previousDisabled: function() {
		return this.get('controller.page') == 1;
	}.property('controller.page'),

	nextDisabled: function() {
		return this.get('controller.content').length < App.Dao.get("pageSize");
	}.property('controller.page')
});