App.FridgeIndexView = Em.View.extend({

	templateName: 'fridge/index',

	didInsertElement : function() {
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
				window.location = "/fridge/" + ui.item.value;
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
		
		konami();
	}

});