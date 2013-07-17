App.PostsView = Ember.CollectionView.extend({
	tagName : 'section',
	elementId : 'fridge',
	itemViewClass : 'App.PostView',
	contentBinding: 'controller.content',
	
	didInsertElement : function() {
		var view = this;
		view.$().droppable({accept: ".post"});
		konami();
		
		$('#search').typeahead({
		    name: 'fridges',
		    minLength : 2,
		    remote: 'search/fridge/?term=%QUERY'
		})
		.on('typeahead:selected', function(e,datum) {
		    view.get('controller').get('target').transitionToRoute('fridge', App.Dao.initSessionData(datum.value));
		});
	}
});