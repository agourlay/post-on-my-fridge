$(function() {
	initPage();
	//reload page every 10 minutes
	setInterval("initPage()", 600000);
});

function initPage(){
	var fridge = $('.fridge');
	fridge.empty();
	colorPickerManagement();
	datePickerManagement();
	$.getJSON("/getPost", function(data) {
		if (data.postPosition != undefined){
			$.each(data.postPosition, function(index,value){
				buildPost(value['id'],value['author'],value['date'],value['content'],value['color']);
				setPositionPost(value['id'],value['left'],value['top']);
			});
		}
	
		$( ".post" ).hide().fadeIn(1000).draggable({ revert: "invalid" , scroll: true });
		
		$( ".newPost" ).draggable({ revert: "invalid" ,scroll: true});
		
		$( ".trash_bin" ).droppable({
			accept: ".post",
			drop: function( event, ui ) {
				$(this).effect("bounce",{ times:3 }, 300);
				ui.draggable.effect("clip",{ times:1 }, 300);
				$.ajax({ 
					url: "/remove?id="+ui.draggable.attr('id')
				});				
			}
		});
		
		$('.trash_bin').mouseout(function() {
			$(this).effect("shake",{ times:1 }, 300);
		});
		
		$( ".fridge" ).droppable({
			accept: ".post,.newPost",
			drop: function( event, ui ) {
				if ( ui.draggable.hasClass('newPost')){
					var myData = {};
					myData ["author"] = $("#author").val();
					myData ["content"] = $("#content").val();
					myData ["captcha"] = $("#captcha").val();
					myData ["color"] = $("#postColor").val();
					myData ["dueDate"] = $("#dueDate").val();
					myData ["positionX"] = (parseInt(ui.draggable.css('left'))) / fridge.width();
					myData ["positionY"] = (parseInt(ui.draggable.css('top'))) / fridge.height();
					$.ajax({url: "/new",data : myData,success : location.reload()});	
				}else{
					var myData = {};
					myData ["id"] = ui.draggable.attr('id');
					myData ["positionX"] = (parseInt(ui.draggable.css('left'))) / fridge.width();
					myData ["positionY"] = (parseInt(ui.draggable.css('top'))) / fridge.height();
					$.ajax({ url: "/update",data : myData});	
				}	
			}
		});		
	});
}

function setPositionPost(id,left,top){
	var fridge = $('.fridge');
	$("#"+id).css({
		'left':left * fridge.width(),
		'top':top * fridge.height()
		});
	
}	

function generatePostContent(id,author,date,content){
	var urlRegexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
	var twitterRegexp = /(http|https):\/\/(twitter.com)\/(#!)\/(\w*)/
	var xmlRegexp = /(http|https):\/\/(.)+(\/feed\/|\/feeds\/|\.xml|rss)$/
	var youtubeRegexp = /(http|https):\/\/(?:www\.)?\w*\.\w*\/(?:watch\?v=)?((?:p\/)?[\w\-]+)/
	var pictureRegexp = /(http|https):\/\/[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(?:\/\S*)?(?:[a-zA-Z0-9_])+\.(?:jpg|jpeg|gif|png)$/
	
	content = jQuery.trim(content);
	var contentArray = content.split(' ');
	if (contentArray.length == 0){
		contentArray[0] = content;
	}
	
	$.each(contentArray, function(index, value) {  
		if (isRegExp(urlRegexp,value)){
			if(isRegExp(xmlRegexp,value)){
				yql = 'http://query.yahooapis.com/v1/public/yql?q=' + encodeURIComponent('select * from xml where url="' + value + '"') + '&format=xml&callback=?';
				$.getJSON( yql,	function(data) {
			        	buildRssFeed(filterData(data.results[0]),value,id,author,date,content,xmlRegexp);
					  }
				);
			}else if(isRegExp(twitterRegexp,value)){
				$.ajax({
					url: "http://api.twitter.com/1/statuses/user_timeline.json",
					dataType: "jsonp",
					cache: false,
					data : buildTwitterDataUrl(value),
					success: function(data) { 
						buildTweet(data,value,id,author,date,content,twitterRegexp);
					  }
					});
			}else if(isRegExp(pictureRegexp,value)){
				replacementPict = "</br><a href="+value+" target= blank ><img  class='post_picture' src="+value+" /></a>";
				content = content.replace(pictureRegexp,replacementPict);
			}else if(isRegExp(youtubeRegexp,value)){
				replacementThumb = generateYoutubeFrame(value);
				content = content.replace(youtubeRegexp,replacementThumb);				
			}else{
				replacement = "<a href="+value+" target= blank>"+value+"</a> ";
				content = content.replace(urlRegexp,replacement);
			}	
		}	
	});	
	return content;
}

function buildPostContent(id,author,date,content,bgColor){
	if (bgColor == undefined){
		bgColor = "f7f083"
	}
	textColor = getTxtColorFromBg(bgColor);
	template = "<div id=${id} class='post' style='background-color:${bgColor};color:${textColor}'><div class='content'>${content}</div><div class='author'>${author}</div><div class='date'><i>${date}</i></div></div>";
	postDiv = template.replace("${id}",id).replace("${bgColor}",bgColor).replace("${textColor}",textColor).replace("${content}",content).replace("${author}",author).replace("${date}",date);
	$('.fridge').append(postDiv);
}

function buildPost(id,author,date,content,color){
	content = generatePostContent(id,author,date,content);
	buildPostContent(id,author,date,content,color)
}

function extractYoutubeVideoId(url){
	youtube_id = url.replace(/^[^v]+v.(.{11}).*/,"$1");
	return youtube_id; 
}

function extractTwitterUser(url){
	var contentArray = url.split('/#!/');
	return contentArray[1];
}

function buildTweet(data,value,id,author,date,content,twitterRegexp){
	tweet = data[0];
	tweetText = tweet['text'];
	if (tweetText.length > 110){
		tweetText = tweetText.substring(0,110) + "..."
	}
	replacement = "<a href="+value+" target= blank >"+extractTwitterUser(value)+"</a> tweets :</br>"+ tweetText;
	content = content.replace(twitterRegexp,replacement);
	updatePostContent(id,content);
}

function buildRssFeed(feed,value,id,author,date,content,xmlRegexp){
	channel = $(feed).children("channel:first");
	title = channel.find("title:first").html();
	item = channel.find("item:first");
	link = jQuery.trim(item.find("link:first").html());
	itemTitle = $(item).find("title").text();
	replacement = "<a href="+link+" target= blank >"+title+"</a> Rss :</br>"+ itemTitle;
	content = content.replace(xmlRegexp,replacement);
	updatePostContent(id,content);
}

function updatePostContent(id,content){
	$('#'+id).find('.content').empty().append(content);
}

function buildTwitterDataUrl(url){
	var myData = {};
	myData ["count"] = "1";
	myData ["user"] = extractTwitterUser(url);
	myData ["trim_user"] = "true";
	myData ["callback"] = "?";
	return myData;
}

function colorPickerManagement(){
	$("#newPost").css("background-color","f7f083");
	
	colorPicker = $.farbtastic("#color-picker").setColor($("#postColor").val());
	$( "#color-dialog" ).dialog({
		autoOpen: false,
		show: "blind",
		hide: "blind",
		zIndex: 1000,
		modal: true,
		buttons: {
			"Pick color": function() {
				updatePostColor(colorPicker.color);
				$( this ).dialog( "close" );
			},
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		}
	});
	
	$('#changeColor').click(function() {
		$( "#color-dialog" ).dialog( "open" );
	});
	
}

function updatePostColor(color){
	$("#postColor").val(color);
	$("#newPost").css("background-color",color);
	textColor = getTxtColorFromBg(color);
	$("#newPost").find("#content").css("color", textColor);
	$("#newPost").find("#author").css("color", textColor);
}

function generateYoutubeFrame(url){
	frame = "<iframe class='youtube-player' type='text/html' width='190' height='150' src='http://www.youtube.com/embed/"+extractYoutubeVideoId(url)+"?modestbranding=1&autohide=1&wmode=opaque frameborder='0'></iframe>";
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
			buttonImage: "/css/images/calendar.gif",
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
