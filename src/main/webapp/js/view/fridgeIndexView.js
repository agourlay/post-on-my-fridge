App.FridgeIndexView = Em.View.extend({
	tagName : 'div',
	elementId : 'global',
	templateName: 'fridge/index',

	//ready : function() {
	//	document.title = "Fridge "+ this.fridgeId;
	//},

	//rssUrl: function() {
	//		return "/fridge/" + this.get('controller').get('content').get('id') + "/rss";
	//}.property(),

	willInsertElement : function(){
		$("#bootstrap-css").attr("disabled", "disabled");
	},

	didInsertElement : function() {
		konami();
		colorPickerManagement();

		$(".newPost").draggable({
			revert : "invalid",
			scroll : true,
			zIndex : 9999
		});

		$("#search").autocomplete({
			source : "/fridge/noid/search",
			delay : 100,
			minLength : 2,
			select : function(event, ui) {
				App.Router.transitionTo('fridge.index',ui.item.value);
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