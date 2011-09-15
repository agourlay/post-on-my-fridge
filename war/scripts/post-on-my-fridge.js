$(function() {
	initPage();
	//reload page every 5 minutes
	setInterval("initPage()", 300000);
});

function initPage(){
	$( ".fridge" ).empty();
	$.getJSON("/getPost", function(data) {
		$.each(data.postPosition, function(index,value){
			buildPost(value['id'],value['author'],value['date'],value['content']);
			setPositionPost(value);
		});
		
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
		
		$('.trash_bin').mouseover(function() {
			$(this).effect("shake",{ times:1 }, 300);
		});
		
		$( ".fridge" ).droppable({
			accept: ".post",
			drop: function( event, ui ) {
				var posX = (parseInt(ui.draggable.css('left')))/$('.fridge').width();
				var posY = (parseInt(ui.draggable.css('top')))/$('.fridge').height();
				$.ajax({ url: "/update?id="+ui.draggable.attr('id')+"&positionX="+posX+"&positionY="+posY});
			}
		});
		
		$( ".post" ).hide().fadeIn(1000);
			
		$( ".post" ).draggable();	
		
		$( ".post" ).draggable({ revert: "invalid" , scroll: true });
	});
}

function setPositionPost(data){
	$("#"+data['id']).css('left',data['left'] * $('.fridge').width());
	$("#"+data['id']).css('top',data['top'] * $('.fridge').height());
}	

function generatePostContent(id,author,date,content){
	var urlRegexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
	var twitterRegexp = /(http|https):\/\/(twitter.com)\/(#!)\/(\w*)/
	var youtubeRegexp = /(http|https):\/\/(?:www\.)?\w*\.\w*\/(?:watch\?v=)?((?:p\/)?[\w\-]+)/
	var pictureRegexp = /(http|https):\/\/[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(?:\/\S*)?(?:[a-zA-Z0-9_])+\.(?:jpg|jpeg|gif|png)$/
	
	content = trim(content);
	var contentArray = content.split(' ');
	if (contentArray.length == 0){
		contentArray[0] = content;
	}
	
	$.each(contentArray, function(index, value) {  
		if (isRegExp(urlRegexp,value)){
			if(isRegExp(youtubeRegexp,value)){
				replacementThumb = generateYoutubeFrame(value);
				content = content.replace(youtubeRegexp,replacementThumb);
			}else if(isRegExp(pictureRegexp,value)){
				replacementPict = "</br><a href="+value+" target= blank ><img  class='post_picture' src="+value+" /></a>";
				content = content.replace(pictureRegexp,replacementPict);
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
			}
			else{
				replacement = "<a href="+value+" target= blank>"+value+"</a> ";
				content = content.replace(urlRegexp,replacement);
			}	
		}	
	});	
	return content;
}

function buildPostContent(id,author,date,content){
	template = "<div id=${id} class='post'><div class='content'>${content}</div><div class='author'>${author}</div><div class='date'><i>${date}</i></div></div>";
	postDiv = template.replace("${id}",id).replace("${content}",content).replace("${author}",author).replace("${date}",date);
	$('.fridge').append(postDiv);
}

function buildPost(id,author,date,content){
	content = generatePostContent(id,author,date,content);
	buildPostContent(id,author,date,content)
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
	alert(content);
	content = content.replace(twitterRegexp,replacement);
	alert(content);
	updatePostContent(id,content);
}

function updatePostContent(id,content){
	$('#'+id).find('.content').empty();
	$('#'+id).find('.content').append(content);
}

function buildTwitterDataUrl(url){
	var myData = {};
	myData ["count"] = "1";
	myData ["user"] = extractTwitterUser(url);
	myData ["trim_user"] = "true";
	myData ["callback"] = "?";
	return myData;
}

function generateYoutubeFrame(url){
	frame = "<iframe class='youtube-player' type='text/html' width='190' height='150' src='http://www.youtube.com/embed/"+extractYoutubeVideoId(url)+"?modestbranding=1&autohide=1 frameborder='0'></iframe>";
	return frame;
}

function creationRequest(){
	var myData = {};
	myData ["author"] = $("#author").val();
	myData ["content"] = $("#content").val();
	myData ["captcha"] = $("#captcha").val();
	$.ajax({ 
		url: "/new",
		data : myData,
		success : function() {
			initPage();
		}
	});	
}

function isRegExp(regExp, content){
	return regExp.test(content);
}
	
function trim (myString){
	return myString.replace(/^\s+/g,'').replace(/\s+$/g,'')
}

