App.PostsView = Ember.CollectionView.extend({
	tagName : 'section',
	elementId : 'fridge',
	itemViewClass : 'App.PostView',
	contentBinding: 'controller.content',
	
	didInsertElement : function() {
		var view = this;
		view.$().droppable({
			accept: ".post, .newPost",
			drop: function(event, ui) {
				var newPostData = {};
				if (ui.draggable.hasClass('newPost')) {
					newPostData.author = $("#author").val();
					newPostData.content = $("#content").val();
					newPostData.color = $("#postColor").val();
					newPostData.positionX = parseInt(ui.draggable.offset().left, 10) / parseInt(view.$().css("width"), 10);
					newPostData.positionY = parseInt(ui.draggable.offset().top, 10) / parseInt(view.$().css("height"), 10);
					newPostData.fridgeId = App.Dao.get('fridgeId');
					newPostData.dueDate = $("#dueDate").val();
					newPostValidation(newPostData);
					console.log("post to be created :" + JSON.stringify(newPostData));
					view.get('controller').createPost(newPostData);
					ui.draggable.animate({
						'left': '5',
						'top': '5'
					}, 'slow', 'linear');
				}
			}
		});
	}
});