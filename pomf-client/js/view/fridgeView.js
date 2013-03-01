App.FridgeView = Em.View.extend({
	tagName : 'div',
	classNames: ['global'],
	contentBinding: 'controller.content',

	watchContent: function() {
		console.log("FridgeView content changed :" + JSON.stringify(this.get('content')));
	}.observes('content'),
	
	rssUrl: function() {
		var fridgeName = this.get('content').get('name');
		return "/api/rss/fridge/" + fridgeName;
	}.property('content.name'),

	didInsertElement : function() {
		document.title = "Fridge "+ this.get('content').get('name');
		konami();
		colorPickerManagement();

		$(".newPost").draggable({
			revert : "invalid",
			scroll : true,
			zIndex : 9999
		});

		$("#search").autocomplete({
			source : "api/search/fridge/",
			delay : 100,
			minLength : 2,
			select : function(event, ui) {
				window.location = "/#/fridge/" + ui.item.value;
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