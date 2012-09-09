App.PostView = Em.View.extend(App.Draggable, {
	post: null,
	tagName: 'article',
	classNames: ['post'],
	uiType: 'draggable',

	relativeDate: function() {
		var date = this.get('post').get('date');
		return moment(date).fromNow();
	}.property('post.date').cacheable(),

	generatedContent: function() {
		return this.generateContent();
	}.property('post.content').cacheable(),

	mouseEnter: function(event) {
		this.$().find(".header").css({
			'position': 'absolute',
			'left': 0,
			'top': '-18px'
		}).fadeIn(300);
	},

	mouseLeave: function(event) {
		var uiElmt = event.currentTarget;
		if (!$(uiElmt).hasClass("header")) {
			this.$().find(".header").fadeOut(300);
		}
	},

	doubleClick: function() {
		alert('Live edit feature coming soon');
	},

	didInsertElement: function() {
		var view = this;

		view.initTrash();
		view.colorize();
		view.updatePhysicalPosition();

		view.$().draggable({
			revert: 'invalid',

			stop: function(event) {
				var fridge = $('#fridge');
				var fullPosition = (parseInt(view.$().offset().left, 10)) / parseInt(fridge.css("width"), 10) + ' ' + (parseInt(view.$().offset().top, 10)) / parseInt(fridge.css("height"), 10);
				view.get('post').set('fullPosition', fullPosition);
			}
		});
	},

	willDestroyElement: function() {
		this.$().effect("bounce", {
			times: 3
		}, 300);
		this.$().effect("clip", {
			times: 1
		}, 300);
		libnotify = humane.create({
			baseCls: 'humane-libnotify',
			addnCls: 'humane-libnotify-info'
		});
		libnotify.log("Post from " + this.get('post').get('author') + " deleted");
	},

	updatePhysicalPosition: function() {
		var left = this.get('post').get('positionX');
		var top = this.get('post').get('positionY');
		var fridge = $('#fridge');

		xTranslation = (left * fridge.width() - parseInt(this.$().offset().left, 10));
		yTranslation = (top * fridge.height() - parseInt(this.$().offset().top, 10));

		if (xTranslation != 0 || yTranslation != 0){
			this.$().animate({
				'left': "+=" + xTranslation,
				'top': "+=" + yTranslation
			}, 'slow', 'linear');
		}
	}.observes('post.fullPosition'),

	colorize: function() {
		var color = this.get('post').get('color');
		this.$().css("background-color", color);
		this.$().css("color", getTxtColorFromBg(color));
	},

	initTrash: function() {
		var view = this;
		view.$().find(" .ui-icon-trash").click(function() {
			if (confirm("Are you sure you want to delete post?")) {
				App.FridgeController.deletePost(view.get('post').get('id'));
			}
		});
	},

	// TODO Arnaud refactor this big sh*t 
	generateContent: function() {
		content = jQuery.trim(this.get('post').get('content'));
		urlRegexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
		twitterRegexp = /(http|https):\/\/(twitter.com)\/(#!)\/(\w*)/;
		rssRegexp = /(http|https):\/\/(.)+(\/feed\/|\/feeds\/|\.xml|rss)$/;
		youtubeRegexp = /(http|https):\/\/(?:www\.)?\w*\.\w*\/(?:watch\?v=)?((?:p\/)?[\w\-]+)/;
		pictureRegexp = /(http|https):\/\/[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(?:\/\S*)?(?:[a-zA-Z0-9_])+\.(?:jpg|JPG|jpeg|gif|png)$/;

		contentArray = content.split(' ');

		if (contentArray.length === 0) {
			contentArray[0] = content;
		}

		$.each(contentArray, function(index, value) {
			if (isRegExp(urlRegexp, value)) {
				var url = purl(value);

				if (url.attr('host') == "twitter.com") {

					$.ajax({
						url: "http://api.twitter.com/1/statuses/user_timeline.json",
						dataType: "jsonp",
						cache: false,
						data: buildTwitterDataUrl(value),
						success: function(data) {
							buildTweet(data, value, author, date, content, twitterRegexp);
						}
					});

				} else if (url.attr('host') == "www.youtube.com") {

					content = content.replace(youtubeRegexp, generateYoutubeFrame(url.param('v')));

				} else if (isRegExp(rssRegexp, value)) {

					yql = 'http://query.yahooapis.com/v1/public/yql?q=' + encodeURIComponent('select * from xml where url="' + value + '"') + '&format=xml&callback=?';
					$.getJSON(yql, function(data) {
						content = buildRssFeed(filterData(data.results[0]), content, rssRegexp);
					});

				} else if (isRegExp(pictureRegexp, value)) {

					replacementPict = "</br><a href=" + value + " target= blank ><img  class='post_picture' src=" + value + " /></a>";
					content = content.replace(pictureRegexp, replacementPict);

				} else {

					replacement = "<a href=" + value + " target= blank>" + value + "</a> ";
					content = content.replace(urlRegexp, replacement);

				}
			}
		});
		return content;
	}
});