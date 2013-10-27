App.MainHeaderView = Em.View.extend({
	templateName: 'mainHeader',
	didInsertElement: function() {
		var view = this;
		$('#search').typeahead({
		    name: 'fridges',
		    minLength : 2,
		    remote: 'search/fridge/?term=%QUERY'
		})
		.on('typeahead:selected', function(e,datum) {
		    view.get('controller').transitionToRoute('fridge', App.Dao.initSessionData(datum.value));
		});
	}
});