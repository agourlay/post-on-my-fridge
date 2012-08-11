App.PostView = Em.View.extend(App.Draggable,{
		content: null,
		tagName: 'article',
		classNames: ['post'],
		uiType: 'draggable',

		mouseEnter: function(event){
        	this.$().find(".header").css({'position':'absolute','left':0,'top':'-18px'}).fadeIn(300);
        },

        mouseLeave : function(event){
	        var uiElmt = event.currentTarget;
	        if(!$(uiElmt).hasClass("header")){
	        	 this.$().find(".header").fadeOut(300);
	        }
        },

  		didInsertElement : function(){
  			var view = this;

  			view.initTrash();
  			view.colorize();
  			view.animate();

  			view.$().draggable({
  				revert: 'invalid',

  				stop : function(event){
  					var fridge = $('#fridge');
  					var fullPosition = (parseInt(view.$().offset().left,10)) / fridge.width() +' '+  (parseInt(view.$().offset().top,10)) / fridge.height();
			        view.get('content').set('fullPosition', fullPosition);
				}
  			});		
  		},

  		willDestroyElement : function(){
  			this.$().effect("bounce",{ times:3 }, 300);
			this.$().effect("clip",{ times:1 }, 300);
			libnotify = humane.create({baseCls: 'humane-libnotify', addnCls: 'humane-libnotify-info'});
			libnotify.log("Post from "+this.get('content').get('author')+" deleted");
  		},

  		animate : function(){
  			var left = this.get('content').get('positionX');
  			var top = this.get('content').get('positionY');
			var fridge = $('#fridge');
	
			xTranslation = (left * fridge.width() - parseInt(this.$().offset().left,10)); 
			yTranslation = (top * fridge.height() - parseInt(this.$().offset().top,10));
	
			this.$().animate({'left': "+="+xTranslation,
					   		   'top': "+="+yTranslation
					  		 },'slow','linear');
		},

		updatePhysicalPosition : function(){
			this.animate();
		}.observes('content.fullPosition'),

		colorize : function (){
			var color = this.get('content').get('color');
  			this.$().css("background-color",color);
  			this.$().css("color",getTxtColorFromBg(color));
		},

		initTrash : function (){
			var view = this;
			view.$().find(" .ui-icon-trash").click(function(){
			if(confirm("Are you sure you want to delete post?")){
				App.FridgeController.deletePost(view.get('content').get('id'));
				}
			});
		},

		doubleClick : function (){
			alert('Live edit feature coming soon')
		},

		relativeDate: function() {
	    	var date = this.get('content').get('date');
	   		return moment(date).fromNow();
	  		}.property('App.Contollers.Fridge.@each.date')
});
