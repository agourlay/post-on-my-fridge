function deleteProcedure(data){
	$.each($('.post'),function(indexPost,valuePost){
		var postId = valuePost['id'],
			exist = _.find(data, function(dataPost){return dataPost['id'] == postId}),
			remove = !exist;

		if (remove){
			deleteAnimationPost(postId);
		}
	});
}


function dropPost(elmtId){
	$.ajax({
		type:'DELETE',
		url: "/resources/post/"+elmtId,
		success:function(){
			$("#"+elmtId).effect("bounce",{ times:3 }, 300, function(){deleteAnimationPost(elmtId);});
		}
	});
}


function replaceNewPost(elmt){
    xTranslation = ( 0.01 * $(document).width() - parseInt(elmt.css('left'),10)); 
    yTranslation = ( -0.006 * $(document).height() - parseInt(elmt.css('top'),10));
    //elmt.animate({'left': "+="+xTranslation, 'top': "+="+yTranslation },'slow','linear');
    elmt.animate({'left': '10', 'top': '10' },'slow','linear');
}

function isFridgeContaining(postId){
	return _.find($('.post'), function(dataPost){return dataPost['id'] == postId});
}

function deleteAnimationPost(elementId){
	element = $("#"+elementId);
	element.effect("clip",{ times:1 }, 300);
	libnotify = humane.create({baseCls: 'humane-libnotify', addnCls: 'humane-libnotify-info'});
	libnotify.log("Post from "+element.find('.author').text()+" deleted");
	element.remove();
}

function createOrUpdate(data){
	$.each(data, function(index,value){
		if (!isFridgeContaining(value.id)){
			buildPost(value);
			setPositionPost(value.id,value.positionX,value.positionY);
			$("#"+value.id).hide().fadeIn(1000).draggable({ revert: "invalid" , scroll: true });
			$("#"+value.id+" .ui-icon-trash").click(function(){
				if(confirm("Are you sure to delete post?")){
					dropPost(value.id);
				}
			});
		}else{
			updateDisplayedPosition(value.id,value.positionX,value.positionY);
		}
	});
}

function updateDisplayedPosition(id,left,top){
	var fridge = $('.fridge');
	
	xTranslation = (left * fridge.width() - parseInt($("#"+id).css('left'),10)); 
	yTranslation = ( top * fridge.height() - parseInt($("#"+id).css('top'),10));
	
	$("#"+id).animate({'left': "+="+xTranslation,
					   'top': "+="+yTranslation
					   },'slow','linear');
}	

function setPositionPost(id,left,top){
	var fridge = $('.fridge'),
		elmt = $("#"+id),
		leftPost = left * fridge.width(),
		topPost = top * fridge.height();
	
	elmt.css({
		'left':leftPost,
		'top':topPost
		});
	
}	

function generatePostContent(post){
	var id = post.id,
	    author = post.author,
	    date = post.date,
	    content = jQuery.trim(post.content),
	
	    urlRegexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/,
	    twitterRegexp = /(http|https):\/\/(twitter.com)\/(#!)\/(\w*)/,
	    xmlRegexp = /(http|https):\/\/(.)+(\/feed\/|\/feeds\/|\.xml|rss)$/,
	    youtubeRegexp = /(http|https):\/\/(?:www\.)?\w*\.\w*\/(?:watch\?v=)?((?:p\/)?[\w\-]+)/,
	    pictureRegexp = /(http|https):\/\/[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(?:\/\S*)?(?:[a-zA-Z0-9_])+\.(?:jpg|jpeg|gif|png)$/,
		
	    contentArray = content.split(' ');
	    
	if (contentArray.length === 0){
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

function buildPostContent(post){
	if (post.color === undefined){
		post.color = "#f7f083";
	}

	post.textColor = getTxtColorFromBg(post.color);
	post.relativeDate = function() {
	    return function(text, render) {
	        return render(humaneDate(text));
	      };
	    };	
	var template = $('#postTemplate').html();
	var output = $.mustache(template, post);
	$('.fridge').append(output);
	//TODO change ugly fix for not escaped html 
	updatePostContent(post.id,post.content);
	
}

function buildPost(post){
	post.content = generatePostContent(post);
	buildPostContent(post);
}

function buildTweet(data,value,id,author,date,content,twitterRegexp){
	tweet = data[0];
	tweetText = tweet.text;
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
	myData.count = "1";
	myData.user = extractTwitterUser(url);
	myData.trim_user = "true";
	myData.callback = "?";
	return myData;
}

function colorPickerManagement(){
	var color = $("#postColor").val(),
		textColor = getTxtColorFromBg(color);
	
	updatePostColor(color);
	
	colorPicker = $.farbtastic("#color-picker").setColor(color);
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