App.MainHeaderView = Em.View.extend({
	templateName: 'mainHeader',
	contentBinding: 'controller.content',

	title: function() {
		return this.get('content') !== undefined ? this.get('content') : "Post On My Fridge";
	}.property('content'),

	didInsertElement: function() {
		var view = this;
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
		    view.get('controller').transitionToRoute('fridge', App.Dao.initSessionData(datum.id));
		});
	}
});