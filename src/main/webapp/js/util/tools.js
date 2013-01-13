function newPostValidation(newPostData) {
	if (newPostData.dueDate !== "") {
		newPostData.dueDate = newPostData.dueDate + 'T00:00:00';
	}

	if (newPostData.author === "") {
		newPostData.author = "Anonymous";
	}

	if (newPostData.content === "") {
		newPostData.content = "What's up";
	}
}

function setTooltips() {
	$('#content').powerTip({
    	placement: 'e'
	});

	$('#newPost').powerTip({
    	placement: 'e'
	});

	$('#author').powerTip({
    	placement: 'e'
	});

	$('#dueDate').powerTip({
    	placement: 'e'
	});

	$('#postColor').powerTip({
    	placement: 'e'
	});

	$('#pseudo').powerTip({
    	placement: 'e'
	});
}

function colorPickerManagement() {
	var color = $("#postColor").val(),
		textColor = getTxtColorFromBg(color);		
	updatePostFormColor(color);
}

function updatePostFormColor(color) {
	$("#postColor").val(color);
	$("#newPost").css("background-color", color);
	var textColor = getTxtColorFromBg(color);
	$("#newPost").find("#content").css("color", textColor);
	$("#newPost").find("#author").css("color", textColor);
}

function setRandomBackGround() {
	var path = "/images/background/",
	    myImages = ['circles.png', 'diagonal-noise.png', 'elastoplast.png', 'elegant_grid.png', 'gold_scale.png', 'light_checkered_tiles.png', 'plaid.png', 'silver_scales.png', 'soft_circle_scales.png', 'wavecut.png'],
	    imageFileNumber = myImages.length,
	    randomNumber = Math.floor(Math.random() * imageFileNumber),
	    imageToAssign = myImages[randomNumber],
	    imageFullPath = path + imageToAssign;
	$('#global').css('background-image', 'url(' + imageFullPath + ')');
}

function generateYoutubeFrame(videoId) {
	return "<iframe class='youtube-player' type='text/html' width='225' height='210' src='http://www.youtube.com/embed/" + videoId + "?wmode=opaque&modestbranding=1&autohide=1' frameborder='0'></iframe>";
}

function generateVimeoFrame(videoId) {
	return "<iframe src='http://player.vimeo.com/video/" + videoId + "' width='225' height='210' frameborder='0'></iframe>";
}

function generateDailyMotionLink(videoId) {
	return "<iframe src='http://www.dailymotion.com/embed/video/" + videoId + "' width='225' height='210' frameborder='0'></iframe>";
}

function generatePictureLink(url) {
	return "</br><a href=" + url + " target= blank ><img  class='post_picture' src=" + url + " /></a>";
}

function generateHrefLink(url) {
	return "<a href=" + url + " target= blank>" + url + "</a> ";
}

function getTxtColorFromBg(color) {
	return isDark(color) ? 'white' : 'black';
}

function isDark(color) {
	var R = parseInt((cutHex(color)).substring(0, 2), 16),
	    G = parseInt((cutHex(color)).substring(2, 4), 16),
	    B = parseInt((cutHex(color)).substring(4, 6), 16);
	return R + G + B < 3 * 256 / 2; // r+g+b should be less than half of max (3 * 256)
}

function cutHex(h) {
	return (h.charAt(0) === "#") ? h.substring(1, 7) : h;
}

function isRegExp(regExp, content) {
	return regExp.test(content);
}

function errorMessage(message){
	jackedup = humane.create({
		baseCls: 'humane-jackedup',
		addnCls: 'humane-jackedup-error'
	});
	jackedup.log(message);
}

function infoMessage(message){
	libnotify = humane.create({
		baseCls: 'humane-libnotify',
		addnCls: 'humane-libnotify-info'
	});
	libnotify.log(message);
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

function konami() {
    $(window).konami(function() {
        jQuery('.post').addClass('barrel_roll');
        setTimeout(function() {
            jQuery('.post').removeClass('barrel_roll');
        }, 4000);  
    }); 
}

(function() {
	  (function($) {
	    return $.fn.konami = function(callback) {
	      var code, keysDown;
	      code = [38, 38, 40, 40, 37, 39, 37, 39, 66, 65];
	      keysDown = [];
	      return $(this).bind('keydown.konami', function(e) {
	        keysDown.push(e.keyCode);
	        if (keysDown.join('') !== code.slice(0, keysDown.length).join('')) {
	          keysDown = [];
	        }
	        if (keysDown.length === code.length) {
	          return callback();
	        }
	      });
	    }
	  })(jQuery);
	}).call(this);
