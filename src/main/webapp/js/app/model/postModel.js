App.Post = Em.Object.extend({
		id : null,
		author : null,
		content : null,
		color : null,
		date : null,
		positionX : null,
		positionY : null,
		dueDate : null,
		fridgeId: null,

		fullPosition: function(key, value) {
		    // getter
		    if (arguments.length === 1) {
		      var positionX = this.get('positionX');
		      var positionY = this.get('positionY');
		      return positionX + ' ' + positionY;
		    // setter
		    } else {
			    var position = value.split(" ");
			    Ember.beginPropertyChanges();
			    this.set('positionX', position[0]);
			    this.set('positionY', position[1]);
			    Ember.endPropertyChanges();
			    return value;
			}
		}.property('positionX', 'positionY'),

		resourceUrl : "/resources/post/",

		deletePost : function(){
			$.ajax({
				type:'DELETE',
				url: this.resourceUrl + this.id
			});
		},

		updatePosition : function(){
			var postPosition = {};
			postPosition.positionX = this.get('positionX');
			postPosition.positionY = this.get('positionY');
			$.ajax({ 
					url: this.resourceUrl + this.id,
					type: "PUT",
					data : postPosition});
		}.observes('fullPosition'),

		createPost : function(){
			var postData = {};
			postData.author = this.get('author');
			postData.content = this.get('content');
			postData.color = this.get('color');
			postData.dueDate = this.get('dueDate');
			postData.positionX = this.get('positionX');
			postData.positionY = this.get('positionY');
			postData.fridgeId = this.get('fridgeId');

			$.ajax({
			        url: this.resourceUrl,
			        data : postData,
			        dataType: "html",
			        contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			        type:"POST",
			        success: App.FridgeController.retrievePost(),
			        error:function (xhr, ajaxOptions, thrownError){
			           	jackedup = humane.create({baseCls: 'humane-jackedup', addnCls: 'humane-jackedup-error'});
			           	jackedup.log("Post not created!");
			        }
		    });	
		}
});	