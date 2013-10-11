function newPostValidation(newPostData) {
	if (newPostData.dueDate !== "") {
		newPostData.dueDate = newPostData.dueDate + 'T00:00:00+00:00';
	}else{
		delete newPostData.dueDate;
	}

	newPostData.date = moment().format("YYYY-MM-DDTHH:mm:ssZZ")

	if (newPostData.author === "") {newPostData.author = "Anonymous";}
}

function generateYoutubeFrame(videoUrl) {
	var videoId = url('?v',videoUrl);
	return "<iframe class='youtube-player' type='text/html' width='225' height='210' src='http://www.youtube.com/embed/" + videoId + "?wmode=opaque&modestbranding=1&autohide=1' frameborder='0'></iframe>";
}

function generateVimeoFrame(videoUrl) {
	var videoId = url('1',videoUrl);
	return "<iframe src='http://player.vimeo.com/video/" + videoId + "' width='225' height='210' frameborder='0'></iframe>";
}

function generateDailyMotionLink(videoUrl) {
	var videoId = (url('2',videoUrl)).split('_')[0];
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

function isUrl(content) {
	var urlRegexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
	return isRegExp(urlRegexp,content);
}

function isPictureUrl(content) {
	var pictureRegexp = /(http|https):\/\/[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(?:\/\S*)?(?:[a-zA-Z0-9_])+\.(?:jpg|JPG|jpeg|gif|png)$/;
	return isRegExp(pictureRegexp,content);
}

function isMediaUrlSupported(urlContent){
	if (isPictureUrl(urlContent)) {
			return true;
	}
	switch (url('domain', urlContent)) {
		case "youtube.com":
		    return true;
		case "vimeo.com":
		    return true;
		case "dailymotion.com":
		    return true;
		default:
		   	return false;
	}
}

function textContainsSupportedMediaUrl(text) {
	return text.split(' ').find(function(word) { return isMediaUrlSupported(word); }) !== undefined ;
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

function getRandomPostInitX() {
    return getRandomArbitary (0.3, 0.5);
     
}

function getRandomPostInitY() {
    return getRandomArbitary (0.1,  0.25);
}

function getRandomArbitary (min, max) {
    return Math.random() * (max - min) + min;
}

function randomColor() {
    return '#'+Math.floor(Math.random()*16777215).toString(16);
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
