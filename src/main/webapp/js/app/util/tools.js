function initUIElement() {
	buildSpinner();
	konami();
	setRandomBackGround();
	colorPickerManagement();
	channelManagement();

	$(".newPost").draggable({
		revert: "invalid",
		scroll: true
	});

	$('#message').keyup(function(e) {
		if (e.which == 13 && !e.shiftKey && !e.ctrlKey) {
			onChatTextAreaChange();
			e.preventDefault();
		}
	});

	$("#search").autocomplete({
		source: "/resources/fridge/noid/search",
		delay: 500,
		minLength: 2,
		select: function(event, ui) {
			 window.location = "/fridge/" + ui.item.label;
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
				newPostData.dueDate = $("#dueDate").val();
				if (newPostData.dueDate != ""){
					newPostData.dueDate = newPostData.dueDate+'T00:00:00';	
				}
				newPostData.positionX = parseInt(ui.draggable.offset().left, 10) / parseInt(fridge.css("width"), 10);
				newPostData.positionY = parseInt(ui.draggable.offset().top, 10) / parseInt(fridge.css("height"), 10);
				newPostData.fridgeId = App.FridgeController.get('fridgeId');
				App.FridgeController.createPost(newPostData);
				ui.draggable.animate({
					'left': '10',
					'top': '10'
				}, 'slow', 'linear');
				App.FridgeController.retrievePost();
			}
		}
	});
}

function buildTweet(data, value, content, twitterRegexp) {
	tweet = data[0];
	tweetText = tweet.text;
	if (tweetText.length > 110) {
		tweetText = tweetText.substring(0, 110) + "...";
	}
	replacement = "<a href=" + value + " target= blank >" + extractTwitterUser(value) + "</a> tweets :</br>" + tweetText;
	content = content.replace(twitterRegexp, replacement);
	return content;
}

function buildRssFeed(feed, content, rssRegexp) {
	channel = $(feed).children("channel:first");
	title = channel.find("title:first").html();
	item = channel.find("item:first");
	link = jQuery.trim(item.find("link:first").html());
	itemTitle = $(item).find("title").text();
	replacement = "<a href=" + link + " target= blank >" + title + "</a> Rss :</br>" + itemTitle;
	content = content.replace(rssRegexp, replacement);
	return content;
}

function buildTwitterDataUrl(url) {
	var myData = {};
	myData.count = "1";
	myData.user = extractTwitterUser(url);
	myData.trim_user = "true";
	myData.callback = "?";
	return myData;
}

function colorPickerManagement() {
	var color = $("#postColor").val(),
		textColor = getTxtColorFromBg(color);
		
	updatePostFormColor(color);
}

function updatePostFormColor(color) {
	$("#postColor").val(color);
	$("#newPost").css("background-color", color);
	textColor = getTxtColorFromBg(color);
	$("#newPost").find("#content").css("color", textColor);
	$("#newPost").find("#author").css("color", textColor);
}


function buildSpinner() {
	var opts = {
		lines: 13,
		// The number of lines to draw
		length: 7,
		// The length of each line
		width: 4,
		// The line thickness
		radius: 10,
		// The radius of the inner circle
		rotate: 0,
		// The rotation offset
		color: '#000',
		// #rgb or #rrggbb
		speed: 1,
		// Rounds per second
		trail: 60,
		// Afterglow percentage
		shadow: false,
		// Whether to render a shadow
		hwaccel: false,
		// Whether to use hardware acceleration
		className: 'spinner',
		// The CSS class to assign to the spinner
		zIndex: 2e9,
		// The z-index (defaults to 2000000000)
		top: 'auto',
		// Top position relative to parent in px
		left: 'auto' // Left position relative to parent in px
	};
	var target = document.getElementById('loading');
	var spinner = new Spinner(opts).spin(target);
}

function messageManagment(user, message) {
	var chatModel = {};
	chatModel.user = user;
	chatModel.message = message;
	chatModel.timestamp = moment().format('h:mm:ss');

	var source = $("#chatMessageTemplate").html();
	var template = Handlebars.compile(source);
	var output = template(chatModel);
	$('#chatLog').append(output);
	$("#chatLog").animate({
		scrollTop: $("#chatLog").prop("scrollHeight")
	}, 3000);
}

function onChatTextAreaChange() {
	sendChatMessage();
	$("#message").val('');
}

function sendChatMessage() {
	var payload = {};
	payload.fridgeId = $("#fridgeId").val();
	payload.message = $("#message").val();
	payload.user = $("#pseudo").val();
	$.ajax({
		type: "POST",
		url: "/_ah/channel/" + payload.fridgeId + "/message",
		data: payload
	});
}

function channelManagement() {
	var fridgeId = $("#fridgeId").val();
	$.getJSON("/_ah/channel/" + fridgeId, function(tokenChannel) {
		if (tokenChannel !== undefined) {
			var channel = new goog.appengine.Channel(tokenChannel);
			var socket = channel.open();
			socket.onopen = function() {

			};
			socket.onmessage = function(m) {
				var data = $.parseJSON(m.data);
				if (data.command == "#FRIDGE-UPATE#") {
					App.FridgeController.retrievePost();
				}
				if (data.command == "#FRIDGE-CHAT#") {
					messageManagment(data.user, data.message);
				}
			};
			socket.onerror = function(err) {
				jackedup = humane.create({
					baseCls: 'humane-jackedup',
					addnCls: 'humane-jackedup-error'
				});
				jackedup.log("Channel error :" + err.description);
			};
			socket.onclose = function() {};
		}
	});
}

function showFridge() {
	$('#loading').remove();
	$('#fridge').css({
		opacity: 0.0,
		visibility: "visible"
	}).animate({
		opacity: 1.0
	});
}

function setRandomBackGround() {
	path = "/images/background/";
	myImages = ['bright_squares.png', 'circles.png', 'diagonal-noise.png', 'elastoplast.png', 'elegant_grid.png', 'gold_scale.png', 'light_checkered_tiles.png', 'noise_pattern_with_crosslines.png', 'plaid.png', 'ravenna.png', 'roughcloth.png', 'silver_scales.png', 'soft_circle_scales.png', 'wavecut.png', 'xv.png'];
	imageFileNumber = myImages.length;
	randomNumber = Math.floor(Math.random() * imageFileNumber);
	imageToAssign = myImages[randomNumber];
	imageFullPath = path + imageToAssign;
	$('#global').css('background-image', 'url(' + imageFullPath + ')');
}

function extractTwitterUser(url) {
	var contentArray = url.split('/#!/');
	return contentArray[1];
}

function generateYoutubeFrame(videoId) {
	return "<iframe class='youtube-player' type='text/html' width='218' height='200' src='http://www.youtube.com/embed/" + videoId + "?wmode=opaque&modestbranding=1&autohide=1 frameborder='0'></iframe>";
}

function getTxtColorFromBg(color) {
	return isDark(color) ? 'white' : 'black';
}

function isDark(color) {
	R = parseInt((cutHex(color)).substring(0, 2), 16);
	G = parseInt((cutHex(color)).substring(2, 4), 16);
	B = parseInt((cutHex(color)).substring(4, 6), 16);
	return R + G + B < 3 * 256 / 2; // r+g+b should be less than half of max (3 * 256)
}

function cutHex(h) {
	return (h.charAt(0) == "#") ? h.substring(1, 7) : h;
}

function isRegExp(regExp, content) {
	return regExp.test(content);
}

function filterData(data) {
	data = data.replace(/<?\/body[^>]*>/g, '');
	data = data.replace(/[\r|\n]+/g, '');
	data = data.replace(/<--[\S\s]*?-->/g, '');
	data = data.replace(/<noscript[^>]*>[\S\s]*?<\/noscript>/g, '');
	data = data.replace(/<script[^>]*>[\S\s]*?<\/script>/g, '');
	data = data.replace(/<script.*\/>/, '');
	return data;
}