App.FridgesView = Em.View.extend({
	tagName : 'div',
    elementId: 'fridgesView',
	newName: null,

	previousDisabled: function() {
		return this.get('controller.page') == 1;
	}.property('controller.page'),

	nextDisabled: function() {
		return this.get('controller.content').length < App.Dao.get("pageSize");
	}.property('controller.page'),

	didInsertElement: function() {
		var view = this;
		document.title = "All fridges | Post on my fridge";
		var fridges = new Bloodhound({
		  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
		  queryTokenizer: Bloodhound.tokenizers.whitespace,
		  remote: 'search/fridge?term=%QUERY'
		});
		fridges.initialize();
		$('.typeahead').typeahead({
		    hint: true,
		    highlight: true,
		    minLength: 2
		},{
		    name: 'fridges',
		    displayKey: 'name',
		    source: fridges.ttAdapter(),
		    templates: {
		        empty: [
			      '<div class="tt-suggestion">',
			      'no fridge matching',
			      '</div>'
			    ].join('\n'),
			    suggestion: function(data){
                return '<p>' + data.name + '</p>';
            	}
			}
		})
		.on('typeahead:selected', function(e, datum, name) {
			App.Dao.leaveChatOnExit();
		    view.get('controller').transitionToRoute('fridge', App.Dao.initSessionData(datum.id));
		});
	}
});