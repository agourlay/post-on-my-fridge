App.MainHeaderView = Em.View.extend({
	templateName: 'mainHeader',
	contentBinding: 'controller.content',

	title: function() {
		return this.get('content') !== undefined ? this.get('content') : "Post On My Fridge";
	}.property('content'),

	didInsertElement: function() {
		var view = this;
		$('#search').typeahead({
		    name: 'fridges',
		    minLength : 2,
		    remote: 'search/fridge?term=%QUERY'
		})
		.on('typeahead:selected', function(e,datum) {
		    view.get('controller').transitionToRoute('fridge', App.Dao.initSessionData(datum.value));
		});
	}
});