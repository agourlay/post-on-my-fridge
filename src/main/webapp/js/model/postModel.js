App.Post = Em.Object.extend({
	id: null,
	author: null,
	content: null,
	color: null,
	date: null,
	positionX: null,
	positionY: null,
	dueDate: null,
	fridgeId: null,
	resourceUrl: function(){
		return "api/fridge/"+ this.get('fridgeId') + "/post/";
	}.property(),

	fullPosition: function(key, value) {
		// getter
		if (arguments.length === 1) {
			var positionX = this.get('positionX');
			var positionY = this.get('positionY');
			return positionX + ' ' + positionY;
		// setter
		} else {
			var position = value.split(" ");
			this.set('positionX', position[0]);
			this.set('positionY', position[1]);
			return value;
		}
	}.property().cacheable(),

	deletePost: function() {
		$.ajax({
			method: 'DELETE',
			url: this.get('resourceUrl') + this.id,
			dataType: "text",
			error: function(xhr, ajaxOptions, thrownError) {
				console.log(thrownError);
				errorMessage("Post not deleted!");
			}
		});
	},

	updatePosition: function() {
		$.ajax({
			url: this.get('resourceUrl'),
			method: "PUT",
			contentType: "application/json",
			dataType: "text",
			data: JSON.stringify(this),
			error: function(xhr, ajaxOptions, thrownError) {
				console.log(thrownError);
				errorMessage("Post not updated!");
			}
		});
	}.observes('fullPosition','content','color'),

	createPost: function() {
		$.ajax({
			url: this.get('resourceUrl'),
			method: "POST",
        	contentType: "application/json",
        	dataType: "text",
        	data: JSON.stringify(this),
			error: function(xhr, ajaxOptions, thrownError) {
				console.log(thrownError);
				errorMessage("Post not created!");
			}
		});
	}
});