$(function() {
	initPage();
	//reload page every 5 minutes
	setInterval("initPage()", 300000);
});

function initPage(){
	$( ".fridge" ).empty();
	$.getJSON("/getPost", function(data) {
		$.each(data.postPosition, function(index,value){
			generatePost(value['id'],value['author'],value['date'],value['content']);
			setPositionPost(value);
		});
		
		$( ".draggable" ).draggable({ revert: "invalid" , scroll: true });
		
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
	});
}

function setPositionPost(data){
	$("#"+data['id']).css('left',data['left'] * $('.fridge').width());
	$("#"+data['id']).css('top',data['top'] * $('.fridge').height());
}	

function generatePostContent(content){
	content = trim(content);
	
	var urlRegexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
	var youtubeRegexp = /(http|https):\/\/(?:www\.)?\w*\.\w*\/(?:watch\?v=)?((?:p\/)?[\w\-]+)/
	var pictureRegexp = /(http|https):\/\/[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(?:\/\S*)?(?:[a-zA-Z0-9_])+\.(?:jpg|jpeg|gif|png)$/
		
	var contentArray = content.split(' ');
	$.each(contentArray, function(index, value) {  
		if (isRegExp(urlRegexp,value)){
			if(isRegExp(youtubeRegexp,value)){
				replacementThumb = generateYoutubeFrame(value);
				content = content.replace(youtubeRegexp,replacementThumb);
			}else if(isRegExp(pictureRegexp,value)){
				replacementPict = "</br><a href="+value+" target= blank ><img  class='post_picture' src="+value+" /></a>";
				content = content.replace(pictureRegexp,replacementPict);
			}else{
				replacement = "<a href="+value+" target= blank>"+value+"</a> ";
				content = content.replace(urlRegexp,replacement);
			}	
		 }
	});
	
	return content;
}

function generatePost(id,author,date,content){
	template = "<div id=${id} class='post draggable'><div class='content'>${content}</div><div class='author'>${author}</div><div class='date'><i>${date}</i></div></div>";
	content = generatePostContent(content);
	postDiv = template.replace("${id}",id).replace("${content}",content).replace("${author}",author).replace("${date}",date);
	$('.fridge').append(postDiv);

}

function extractYoutubeVideoId(url){
	var youtube_id;
	youtube_id = url.replace(/^[^v]+v.(.{11}).*/,"$1");
	return youtube_id; 
}

function generateYoutubeFrame(url){
	frame = "<iframe class='youtube-player' type='text/html' width='190' height='150' src='http://www.youtube.com/embed/"+extractYoutubeVideoId(url)+"?modestbranding=1&autohide=1 frameborder='0'></iframe>";
	return frame;
}

function creationRequest(){
	$.ajax({ 
		url: "/new?author="+$("#author").val()+"&content="+$("#content").val()+"&captcha="+$("#captcha").val(),
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

