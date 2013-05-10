App.PostsView = Ember.CollectionView.extend({
	tagName : 'section',
	elementId : 'fridge',
	itemViewClass : 'App.PostView',
	contentBinding: 'controller.content',
	
	didInsertElement : function() {
		var view = this;
		view.$().droppable({accept: ".post"});
		konami();
		
		$("#search").autocomplete({
			source : "api/search/fridge/",
			delay : 100,
			minLength : 2,
			select : function(event, ui) {
				view.get('controller').get('target').transitionToRoute('fridge', App.Dao.initSessionData(ui.item.value));
			},
			open: function (event, ui) {
		        $('.ui-autocomplete').css('z-index', '99999');
		    },
			response : function(event, ui) {
				if (ui.content.length === 0) {
					ui.content.push({
						label : "Click to create",
						value : $("#search").val()
					});
				}
			}
		});
	}
});