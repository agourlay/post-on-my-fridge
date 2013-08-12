App.PanelView = Ember.View.extend({
	tagName: 'aside',
	elementId: 'panel',
	contentBinding: 'controller.content',

	fridgeName: function() {
		return this.get('content.id');
	}.property('content.id'),

	rssUrl: function() {
		var fridgeName = this.get('content.id');
		document.title = "Fridge "+ fridgeName;
		return "/rss/fridge/" + fridgeName;
	}.property('content.id'),

	didInsertElement : function() {	
		var view = this;
		if(typeof store.get('username') !== "undefined"){
			$("#pseudo").val(store.get('username'));
		}

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