App.MainHeaderView = Em.View.extend({
	templateName: 'mainHeader',
	contentBinding: 'controller.content',

	title: function() {
		return this.get('content') !== undefined ? this.get('content') : "Post On My Fridge";
	}.property('content'),

	didInsertElement: function() {
		var view = this;
		var fridges = new Bloodhound({
		  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
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
		    displayKey: 'value',
		    source: fridges.ttAdapter(),
		    templates: {
		        empty: [
			      '<div>',
			      'no fridge matching query',
			      '</div>'
			    ].join('\n'),
			    suggestion: function(data){
                return '<p>' + data + '</p>';
            }
			}
		})
		.on('typeahead:selected', function(e,datum,name) {
		    view.get('controller').transitionToRoute('fridge', App.Dao.initSessionData(datum));
		});
	}
});