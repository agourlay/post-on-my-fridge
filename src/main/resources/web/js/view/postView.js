App.PostView = Em.View.extend(App.Draggable, {
	tagName: 'article',
	classNames: ['post'],
	uiType: 'draggable',
	templateName: 'post',
    readMode : true,

	relativeDate: function() {
		var date = this.get('content').get('date');
		return moment(date).fromNow();
	}.property('content.date').cacheable(),

	generatedContent: function() {
		return this.generateContent();
	}.property('content.content').cacheable(),

	mouseEnter: function(event) {
		this.$().find(".header").css({
			'display': 'block'
		}).fadeIn(300);
	},

	mouseLeave: function(event) {
		var uiElmt = event.currentTarget;
		if (!$(uiElmt).hasClass("header")) {
			this.$().find(".header").fadeOut(300);
		}
	},

	didInsertElement: function() {
		var view = this;
		view.initIcons();
		view.colorize();
		view.updatePhysicalPosition();
		view.$().draggable({
			revert: 'invalid',
			containment: "parent",
			stop: function(event) {
				var fridge = $('#fridge-content'),
				    fullPosition = view.$().offset().left / fridge.width() + ' ' + view.$().offset().top / fridge.height();
				view.get('content').set('fullPosition', fullPosition);
				view.get('content').updatePost()
			}
		});
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

	initIcons: function() {
		var view = this;
		// Trash
		view.$().find(" .icon-trash").click(function() {
			view.$().effect("bounce", {	times: 2}, 300)
				    .effect("clip", { times: 1}, 300, function(){
				    	infoMessage("Post from " + view.get('content').get('author') + " deleted");
						view.get('controller').deletePost(view.get('content').get('id'));
				    });
		});
		// Edit
		view.$().find(" .icon-edit").click(function() {
			view.set('readMode',!view.get('readMode'));
		});
	},

	save: function(e) {
    	this.get('content').setProperties({
    		color: this.get('textFieldColor.value'), 
    		author: this.get('textFieldAuthor.value'), 
    		content: this.get('textFieldContent.value'), 
    		dueDate: this.get('textFieldDueDate.value')
    	});
    	this.set('readMode',!this.get('readMode'));
  	},

	generateContent: function() {
		var content = jQuery.trim(this.get('content').get('content')),
		    urlRegexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/,
            pictureRegexp = /(http|https):\/\/[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(?:\/\S*)?(?:[a-zA-Z0-9_])+\.(?:jpg|JPG|jpeg|gif|png)$/,
		    firstWordUrl = content.split(' ').find(function(word) { return isRegExp(urlRegexp, word); });
		if (firstWordUrl === undefined) {
			return content;
		}else {
			if (isRegExp(pictureRegexp, firstWordUrl)) {
				return generatePictureLink(firstWordUrl);
			}	
			switch (url('domain', firstWordUrl)) {
			    case "youtube.com":
			        return generateYoutubeFrame(url('?v',firstWordUrl));
			    case "vimeo.com":
			        return generateVimeoFrame(url('1',firstWordUrl));
			    case "dailymotion.com":
			        return generateDailyMotionLink((url('2',firstWordUrl)).split('_')[0]);
			    default:
			    	return generateHrefLink(firstWordUrl);
			}
		}
	}
});