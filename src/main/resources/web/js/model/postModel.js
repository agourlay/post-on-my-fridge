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
		return "posts/";
	}.property(),

	resourceUrlWithToken: function(){
		return this.get("resourceUrl") +"?token=" + App.Dao.get("userToken") ;
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
			this.set('positionX', parseFloat(position[0]));
			this.set('positionY', parseFloat(position[1]));
			return value;
		}
	}.property().cacheable(),

	deletePost: function() {
		$.ajax({
			method: 'DELETE',
			url:  this.get("resourceUrl") + this.id+"?token=" + App.Dao.get("userToken"),
			dataType: "text",
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during post deletion");
			}
		});
	},

	updatePost: function() {
		$.ajax({
			url: this.get('resourceUrlWithToken'),
			method: "PUT",
			contentType: "application/json",
			dataType: "text",
			data: JSON.stringify(this),
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during post update");
			}
		});
	}.observes('content','color','author','dueDate'),

	createPost: function() {
		return $.ajax({
			url: this.get('resourceUrlWithToken'),
			method: "POST",
        	contentType: "application/json",
        	dataType: "text",
        	data: JSON.stringify(this),
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during post creation");
			}
		});
	}
});