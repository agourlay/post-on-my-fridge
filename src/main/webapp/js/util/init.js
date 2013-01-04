function initUIElement() {
	konami();
	setRandomBackGround();
	colorPickerManagement();
	setTooltips();

	$(".newPost").draggable({
		revert: "invalid",
		scroll: true,
		stack: "div"
	});

    $("#search").autocomplete({
		source: "/fridge/noid/search",
		delay: 300,
		minLength: 2,
		select: function(event, ui) {
            window.location = "/fridge/" + ui.item.value;
        },
	    response: function(event, ui) {
	        if (ui.content.length === 0) {
                ui.content.push({ label: "Click to create", value: $("#search").val()});
	        }
	    }
	});

	var fridge = $('#fridge');

	fridge.droppable({
		accept: ".post, .newPost",
		drop: function(event, ui) {
			var newPostData = {};
			if (ui.draggable.hasClass('newPost')) {
				newPostData.author = $("#author").val();
				newPostData.content = $("#content").val();
				newPostData.color = $("#postColor").val();
				newPostData.positionX = parseInt(ui.draggable.offset().left, 10) / parseInt(fridge.css("width"), 10);
				newPostData.positionY = parseInt(ui.draggable.offset().top, 10) / parseInt(fridge.css("height"), 10);
				newPostData.fridgeId = App.get('fridgeId');
				newPostData.dueDate = $("#dueDate").val();
				newPostValidation(newPostData);
				App.FridgeController.createPost(newPostData);
				ui.draggable.animate({
					'left': '10',
					'top': '10'
				}, 'slow', 'linear');
			}
		}
	});
}