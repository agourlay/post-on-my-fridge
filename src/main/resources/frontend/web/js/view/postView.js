App.PostView = Em.View.extend({
	tagName: 'article',
	classNames: ['post'],
	templateName: 'post',
	classNameBindings: ['isDragged:rotate-5'],
    isDragged : false,
    readMode : true,

	relativeDate: function() {
		var date = this.get('content').get('date');
		return moment(date, "YYYY-MM-DDTHH:mm:ssZZ").fromNow();
	}.property('content.date').cacheable(),

	generatedContent: function() {
		return this.generateContent();
	}.property('content.content').cacheable(),

	actions: {  
		toggleEditMode : function() {
			this.set('readMode',!this.get('readMode'));
		},
		
		trashPost : function () {
			var view = this;
			view.$() 
			    .effect("highlight")
			    .effect("clip", 300, function(){
				    infoMessage("Post from " + view.get('content').get('author') + " deleted");
					view.get('controller').deletePost(view.get('content').get('id'));
				});
		},

		save: function(e) {
	    	this.get('content').setProperties({
	    		color: this.get('textFieldColor.value'), 
	    		author: this.get('textFieldAuthor.value'), 
	    		content: this.get('textFieldContent.value')
	    	});
	    	this.send('toggleEditMode');
	  	},
	},

	doubleClick: function(event) {
		this.toggleEditMode();
		event.stopPropagation();
	},

	click: function(event) {
		event.stopPropagation();
	},	

	dragStart: function(event) {
		this.set("isDragged", true);
	},

	mouseEnter: function(event) {
		this.$().find(".post-header").css({
			'display': 'block'
		}).fadeIn(200);
	},

	mouseLeave: function(event) {
		var uiElmt = event.currentTarget;
		if (!$(uiElmt).hasClass("post-header")) {
			this.$().find(".post-header").fadeOut(200);
		}
	},

	didInsertElement: function() {
		var view = this;
		view.colorize();
		view.setupPosition();
		view.$().draggable({
			revert: 'invalid',
			containment: "parent",
			stop: function(event) {
				var fridge = $('#fridge-content'),
				    fullPosition = view.$().offset().left / fridge.width() + ' ' + view.$().offset().top / fridge.height();
				view.get('content').set('fullPosition', fullPosition);
				view.get('content').updatePost();
				view.set("isDragged", false);
			}
		});

		$(window).resize(function() {
  			view.updatePhysicalPosition();
		});
		
		var mc = new Hammer(this.get('element'));
		mc.on("doubletap", function(ev) {
		    view.send('toggleEditMode');
		    ev.stopPropagation();
		});
	},

	setupPosition: function() {
		var left = this.get('content').get('positionX'),
	    	top = this.get('content').get('positionY'),
	    	fridge = $('#fridge-content');

	    this.$().offset({ "left" : left * fridge.width(), "top" : top * fridge.height()})
	    		.hide()
	    	    .fadeIn();
	},

	updatePhysicalPosition: function() {
		var left = this.get('content').get('positionX'),
		    top = this.get('content').get('positionY'),
		    fridge = $('#fridge-content'),
		    xTranslation = (left * fridge.width() - parseInt(this.$().offset().left, 10)),
		    yTranslation = (top * fridge.height() - parseInt(this.$().offset().top, 10));

		if (xTranslation !== 0 || yTranslation !== 0) {
			this.$().animate({
				'left': "+=" + xTranslation,
				'top': "+=" + yTranslation
			}, 'fast', 'swing');
		}
	}.observes('content.fullPosition'),

	colorize: function() {
		var color = this.get('content').get('color');
		this.$().css("background-color", color);
		this.$().css("color", getTxtColorFromBg(color));
	}.observes('content.color'),

	generateContent: function() {
		var content = jQuery.trim(this.get('content').get('content'));
				
		if (textContainsSupportedMediaUrl(content)){
			firstWordUrl = content.split(' ').find(function(word) { return isUrl(word); });

			if (isPictureUrl(firstWordUrl)) {
				return generatePictureLink(firstWordUrl);
			}

			switch (url('domain', firstWordUrl)) {
			    case "youtube.com":
			        return generateYoutubeFrame(firstWordUrl);
			    case "vimeo.com":
			        return generateVimeoFrame(firstWordUrl);
			    case "dailymotion.com":
			        return generateDailyMotionLink(firstWordUrl);
			}        
		} else {
			// it does not contain supported media url, we just replace all url by href
			genArray = $.map( content.split(' '), function( word, i ) {
			  return (isUrl(word)) ? generateHrefLink(word) : word
			});
			return genArray.join(' ');;
		}
	}
});