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
	resourceUrl: "/resources/post/",

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
			this.notifyPropertyChange('fullPosition');
			return value;
		}
	}.property().cacheable(),

	deletePost: function() {
		$.ajax({
			type: 'DELETE',
			url: this.resourceUrl + this.id,
			error: function(xhr, ajaxOptions, thrownError) {
				jackedup = humane.create({
					baseCls: 'humane-jackedup',
					addnCls: 'humane-jackedup-error'
				});
				jackedup.log("Post not deleted!");
			}
		});
	},

	updatePosition: function() {
		$.ajax({
			url: this.resourceUrl,
			type: "PUT",
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify(this),
			error: function(xhr, ajaxOptions, thrownError) {
				jackedup = humane.create({
					baseCls: 'humane-jackedup',
					addnCls: 'humane-jackedup-error'
				});
				jackedup.log("Post not updated!");
			}
		});
	}.observes('fullPosition'),

	createPost: function() {
		$.ajax({
			url: this.resourceUrl,
			data: JSON.stringify(this),
			dataType: "json",
			type: "POST",
        	contentType: "application/json",
			success: App.FridgeController.retrievePost(),
			error: function(xhr, ajaxOptions, thrownError) {
				jackedup = humane.create({
					baseCls: 'humane-jackedup',
					addnCls: 'humane-jackedup-error'
				});
				jackedup.log("Post not created!");
			}
		});
	}
});