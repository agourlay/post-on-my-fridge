function showPage(){
    $('#loading').hide();
    $('#global').css({opacity: 0.0, visibility: "visible"}).animate({opacity: 1.0})
}

function konami(){
    $(window).konami(function(){
        jQuery('body').addClass('barrel_roll');
        setTimeout(function(){
            jQuery('body').removeClass('barrel_roll');
        },4000);  
    }); 
}    

function generateCaptcha(){
    $(".ajax-fc-container").captcha({
		borderColor: "silver",
		captchaDir: "/css/lib/captcha/images", 
		formId: "postForm",
		url : "/getCaptchaNumber",
		text: "Verify that you are a human,<br />drag <span>scissors</span> into the circle.",
		items: Array("pencil", "scissors", "clock", "heart", "note") 
	});
}

function setRandomBackGround(){
	path = "/images/background/";
	myImages = ['bright_squares.png', 'circles.png', 'diagonal-noise.png', 'elastoplast.png',
	            'elegant_grid.png','gold_scale.png','light_checkered_tiles.png',
	            'noise_pattern_with_crosslines.png','plaid.png','ravenna.png',
	            'roughcloth.png','silver_scales.png','soft_circle_scales.png',
	            'wavecut.png','xv.png'] 
	imageFileNumber = myImages.length;
	randomNumber = Math.floor(Math.random() * imageFileNumber);
	imageToAssign = myImages[randomNumber];
	imageFullPath = path + imageToAssign;
	$('body').css('background-image', 'url(' + imageFullPath + ')');
}

function extractYoutubeVideoId(url){
	youtube_id = url.replace(/^[^v]+v.(.{11}).*/,"$1");
	return youtube_id; 
}

function extractTwitterUser(url){
	var contentArray = url.split('/#!/');
	return contentArray[1];
}

function generateYoutubeFrame(url){
	frame = "<iframe class='youtube-player' type='text/html' width='190' height='150' src='http://www.youtube.com/embed/"+extractYoutubeVideoId(url)+"?wmode=opaque&modestbranding=1&autohide=1 frameborder='0'></iframe>";
	return frame;
}

function getTxtColorFromBg(color){
	return isDark(color) ? 'white' : 'black';
}

function isDark( color ) {
    R = parseInt((cutHex(color)).substring(0,2),16);
    G = parseInt((cutHex(color)).substring(2,4),16);
    B = parseInt((cutHex(color)).substring(4,6),16);
    return R + G + B < 3 * 256 / 2; // r+g+b should be less than half of max (3 * 256)
}

function datePickerManagement(){
		$( "#dueDate" ).datepicker({
			showOn: "button",
			buttonImage: "/images/calendar.gif",
			buttonText: 'Choose a due date',
			buttonImageOnly: true
		});
}

function cutHex(h) {return (h.charAt(0)=="#") ? h.substring(1,7):h}

function isRegExp(regExp, content){
	return regExp.test(content);
}

function filterData(data){
    // filter all the nasties out
    // no body tags
    data = data.replace(/<?\/body[^>]*>/g,'');
    // no linebreaks
    data = data.replace(/[\r|\n]+/g,'');
    // no comments
    data = data.replace(/<--[\S\s]*?-->/g,'');
    // no noscript blocks
    data = data.replace(/<noscript[^>]*>[\S\s]*?<\/noscript>/g,'');
    // no script blocks
    data = data.replace(/<script[^>]*>[\S\s]*?<\/script>/g,'');
    // no self closing scripts
    data = data.replace(/<script.*\/>/,'');
    // [... add as needed ...]
    return data;
  }
