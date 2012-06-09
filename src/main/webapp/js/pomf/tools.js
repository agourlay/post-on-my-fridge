function messageManagment(user,message){
	$("#chatLog").append("<br/>" +user+" : "+message);
	$("#chatLog").scrollTop(parseInt($("#chatLog").scrollHeight,10));
}

function sendChatMessage(){
	var payload = {}; 
	payload.fridgeId = $("#fridgeId").val();
	payload.message = $("#message").val();
	payload.user = $("#pseudo").val();
	$.ajax({
		type: "POST",
		url: "/_ah/channel/"+payload.fridgeId+"/message",
		data: payload
	});
}

function channelManagement(){
	var fridgeId = $("#fridgeId").val();
	$.getJSON("/_ah/channel/"+fridgeId, function(tokenChannel) {
		if (tokenChannel !== undefined){
			var channel = new goog.appengine.Channel(tokenChannel);
			var socket = channel.open();
			socket.onopen = function(){
				
			};
			socket.onmessage = function(m){
				var data = $.parseJSON(m.data);
			    if (data.command == "#FRIDGE-UPATE#"){
			    	initPage();
			    }
			    if (data.command == "#FRIDGE-CHAT#"){
			    	messageManagment(data.user,data.message);
			    }
			};
			socket.onerror =  function(err){
            	jackedup = humane.create({baseCls: 'humane-jackedup', addnCls: 'humane-jackedup-error'});
            	jackedup.log("Websocket error :"+err.description);
			};
			socket.onclose =  function(){};
		}
	});
}	

function messageContain(message,test){
	if (message.indexOf(test) != -1){
		return true
	}else{
		return false;
	}
}

function showPage(){
    $('#loading').hide();
    $('#global').css({opacity: 0.0, visibility: "visible"}).animate({opacity: 1.0});
}

function redirectAfterSearch(){
	var fridgeId = $("#search").val();
	window.location = "/fridge/"+fridgeId;
}

function setupSearchAutocomplete(){
	$( "#search" ).autocomplete({
	    source: "/resources/fridge/noid/search",
	    delay: 1000,
	    minLength: 2
	});
}

function clearText(thefield){
    if (thefield.defaultValue==thefield.value)
    	thefield.value = "";
} 


function setRandomBackGround(){
	path = "/images/background/";
	myImages = ['bright_squares.png', 'circles.png', 'diagonal-noise.png', 'elastoplast.png',
	            'elegant_grid.png','gold_scale.png','light_checkered_tiles.png',
	            'noise_pattern_with_crosslines.png','plaid.png','ravenna.png',
	            'roughcloth.png','silver_scales.png','soft_circle_scales.png',
	            'wavecut.png','xv.png'] ;
	imageFileNumber = myImages.length;
	randomNumber = Math.floor(Math.random() * imageFileNumber);
	imageToAssign = myImages[randomNumber];
	imageFullPath = path + imageToAssign;
	$('#global').css('background-image', 'url(' + imageFullPath + ')');
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
	frame = "<iframe class='youtube-player' type='text/html' width='218' height='200' src='http://www.youtube.com/embed/"+extractYoutubeVideoId(url)+"?wmode=opaque&modestbranding=1&autohide=1 frameborder='0'></iframe>";
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

function cutHex(h) {return (h.charAt(0)=="#") ? h.substring(1,7):h};

function isRegExp(regExp, content){
	return regExp.test(content);
}

function filterData(data){
    data = data.replace(/<?\/body[^>]*>/g,'');
    data = data.replace(/[\r|\n]+/g,'');
    data = data.replace(/<--[\S\s]*?-->/g,'');
    data = data.replace(/<noscript[^>]*>[\S\s]*?<\/noscript>/g,'');
    data = data.replace(/<script[^>]*>[\S\s]*?<\/script>/g,'');
    data = data.replace(/<script.*\/>/,'');
    return data;
}
