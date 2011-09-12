$(function() {
	initPage();
	//reload posts every 5 minutes
	setInterval("refreshContent()", 300000);
});

function initPage(){
	$("#injected_post").load("/posts.jsp #generated_post", function() {
		
		$( ".draggable" ).draggable({ revert: "invalid" , scroll: true });
		
		$( ".trash_bin" ).droppable({
			accept: ".post",
			drop: function( event, ui ) {
				$(this).effect("bounce",{ times:3 }, 300);
				ui.draggable.effect("clip",{ times:1 }, 300);
				$.ajax({ 
					url: "/remove?id="+ui.draggable.attr('id'),
					success : function() {
						refreshContent();
					}
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
			
		$( ".post" ).draggable({
			stop: function() {
			
			}
		});	
		
		$(".post").each(function(index,value) {
			getPositionPost($(this));
			generateContent($(this));
		});
		
	});	
}

function refreshContent(){
	$("#injected_post").load("/posts.jsp #generated_post", function() {
		$(".post").each(function(index,value) {
			getPositionPost($(this));
			generateContent($(this));
		});
	});	
}

function getPositionPost(elmt){
	$.getJSON("/getPositionPost?id="+elmt.attr('id'), function(data) {
		elmt.css('left',data['left'] * $('.fridge').width());
		elmt.css('top',data['top'] * $('.fridge').height());
	});
}	


function generateContent(elmt){
	content = elmt.find('.content').text();
	content = trim(content);
	
	var urlRegexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
	var youtubeRegexp = /(http|https):\/\/(?:www\.)?\w*\.\w*\/(?:watch\?v=)?((?:p\/)?[\w\-]+)/
	var pictureRegexp = /(http|https):\/\/[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(?:\/\S*)?(?:[a-zA-Z0-9_])+\.(?:jpg|jpeg|gif|png)$/
		
	var contentArray = content.split(' ');
	$.each(contentArray, function(index, value) {  
		if (isRegExp(urlRegexp,value)){
			if(isRegExp(youtubeRegexp,value)){
				youtubeThumb = $.jYoutube(value,'small');
				replacementThumb = "</br><a href="+value+" target= blank ><img src="+youtubeThumb+" /></a>";
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
	
	elmt.find('.content').empty();
	elmt.find('.content').append(content);
}

function isRegExp(regExp, content){
	return regExp.test(content);
}
	
function trim (myString){
	return myString.replace(/^\s+/g,'').replace(/\s+$/g,'')
}	

function creationRequest(){
	$.ajax({ 
		url: "/new?author="+$("#author").val()+"&content="+$("#content").val()+"&captcha="+$("#captcha").val(),
		success : function() {
			refreshContent();
		}
	});	
}