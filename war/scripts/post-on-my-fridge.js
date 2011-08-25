$(function() {
		$( ".draggable" ).draggable({ revert: "invalid" , scroll: true });
		
		$( ".trash_bin" ).droppable({
			accept: ".post",
			drop: function( event, ui ) {
				$(this).effect("bounce",{ times:3 }, 300);
				$.ajax({ url: "/remove?id="+ui.draggable.attr('id')});
				ui.draggable.effect("clip",{ times:1 }, 300);
			}
		});
		
		$('.trash_bin').mouseover(function() {
			$(this).effect("shake",{ times:1 }, 300);
		});
		
		$( ".fridge" ).droppable({
			accept: ".post",
			drop: function( event, ui ) {
				var posX = (parseInt(ui.draggable.css('left')))/$(document).width();
				var posY = (parseInt(ui.draggable.css('top')))/$(document).height();
				$.ajax({ url: "/update?id="+ui.draggable.attr('id')+"&positionX="+posX+"&positionY="+posY});
			}
		});
		
		$( ".post" ).hide().fadeIn(1000);
			
		$( ".post" ).draggable({
			stop: function() {
			
			}
		});
		
		
		$(".post").each(function(index) {
			generateContent($(this));
		  });
	        
	});
	

	function generateContent(elmt){
		content = elmt.find('.content').text();
		content = trim(content);
		
		var urlRegexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
		var youtubeRegexp = /(ftp|http|https):\/\/(?:www\.)?\w*\.\w*\/(?:watch\?v=)?((?:p\/)?[\w\-]+)/
		var pictureRegexp = /(ftp|http|https):\/\/[a-zA-Z0-9\-\.]+\.[a-zA-Z]{2,3}(?:\/\S*)?(?:[a-zA-Z0-9_])+\.(?:jpg|jpeg|gif|png)$/
			
		var contentArray = content.split(' ');
		$.each(contentArray, function(index, value) {  
			if (isRegExp(urlRegexp,value)){
				if(isRegExp(youtubeRegexp,value)){
					youtubeThumb = $.jYoutube(value,'small');
					replacementThumb = "</br><a href="+value+"><img src="+youtubeThumb+" /></a>";
					content = content.replace(youtubeRegexp,replacementThumb);
				}else if(isRegExp(pictureRegexp,value)){
					replacementPict = "</br><a href="+value+"><img  class='post_picture' src="+value+" /></a>";
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

	
	