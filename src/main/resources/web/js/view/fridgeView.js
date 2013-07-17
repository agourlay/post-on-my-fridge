App.FridgeView = Em.View.extend({
	tagName : 'div',
	classNames: ['global'],
	contentBinding: 'controller.content',
	
	rssUrl: function() {
		var fridgeName = this.get('content').get('id');
		return "/rss/fridge/" + fridgeName;
	}.property('content.id'),

	didInsertElement : function() {
		var view = this;
		document.title = "Fridge "+ view.get('content').get('id');

		$('#search').typeahead({
		    name: 'fridges',
		    minLength : 2,
		    remote: 'search/fridge/?term=%QUERY'
		})
		.on('typeahead:selected', function(e,datum) {
			debugger;
		    view.get('controller').transitionToRoute('fridge', App.Dao.initSessionData(datum.value));
		});
	}
});