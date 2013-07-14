function newPostValidation(newPostData) {
	if (newPostData.dueDate !== "") {
		newPostData.dueDate = newPostData.dueDate + 'T00:00:00';
	}else{
		delete newPostData.dueDate;
	}

	newPostData.date = moment().format("YYYY-MM-DDTHH:mm:ss");

	if (newPostData.author === "") {newPostData.author = "Anonymous";}

	if (newPostData.content === "") {newPostData.content = "What's up";	}
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

function successMessage(message){
	Alertify.log.success(message);
}

function errorMessage(message){
	Alertify.log.error(message);
}

function infoMessage(message){
	Alertify.log.info(message);
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
        $('.post').addClass('barrel_roll');
        setTimeout(function() {
            $('.post').removeClass('barrel_roll');
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
