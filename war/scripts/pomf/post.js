function deleteProcedure(data){
	$.each($('.post'),function(indexPost,valuePost){
		remove = true;
		$.each(data.postList, function(index,value){
			if (valuePost['id'] == value['id'] ){
				remove = false;
			}
		});
		if (remove){
			deleteAnimationPost(valuePost['id']);
		}
	});
}

function isFridgeContaining(postId){
	result = false;
	$.each($('.post'),function(indexPost,valuePost){
		if (valuePost['id'] == postId ){
			result = true;
		}
	});
	return result;
}

function deleteAnimationPost(elementId){
	element = $("#"+elementId);
	element.effect("clip",{ times:1 }, 300);
	$.jGrowl("Post from "+element.find('.author').text()+" deleted");
	element.remove();
}

function createOrUpdate(data){
	$.each(data.postList, function(index,value){
		if (!isFridgeContaining(value['id'])){
			buildPost(value['id'],value['author'],value['date'],value['content'],value['color'],value['dueDate']);
			setPositionPost(value['id'],value['left'],value['top']);
			$("#"+value['id']).hide().fadeIn(1000).draggable({ revert: "invalid" , scroll: true });
		}else{
			updateDisplayedPosition(value['id'],value['left'],value['top']);
		}
	});
}

function updateDisplayedPosition(id,left,top){
	var fridge = $('.fridge');
	
	xTranslation = (left * fridge.width() - parseInt($("#"+id).css('left'))); 
	yTranslation = ( top * fridge.height() - parseInt($("#"+id).css('top')));
	
	$("#"+id).animate({'left': "+="+xTranslation,
					   'top': "+="+yTranslation
					   },'slow','linear');
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

function buildPostContent(id,author,date,content,bgColor,dueDate){
	if (bgColor == undefined){
		bgColor = "f7f083"
	}
	
	textColor = getTxtColorFromBg(bgColor);
	
	if (dueDate != ""){
		date = date +" and due "+dueDate;
	}
	
	template = "<div id=${id} class='post' style='background-color:${bgColor};color:${textColor}'><div class='content'>${content}</div><div class='author'>${author}</div><div class='date'><i>posted ${date}</i></div></div>";	
	postDiv = template.replace("${id}",id).replace("${bgColor}",bgColor).replace("${textColor}",textColor).replace("${content}",content).replace("${author}",author).replace("${date}",date);
	$('.fridge').append(postDiv);
}

function buildPost(id,author,date,content,color,dueDate){
	content = generatePostContent(id,author,date,content);
	buildPostContent(id,author,date,content,color,dueDate)
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