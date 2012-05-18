$.fn.captcha = function(options){
					
	$(this).html("<b class='ajax-fc-rtop'><b class='ajax-fc-r1'></b> <b class='ajax-fc-r2'></b> <b class='ajax-fc-r3'></b> <b class='ajax-fc-r4'></b></b><img class='ajax-fc-border' id='ajax-fc-left' src='" + options.captchaDir + "/border-left.png' /><img class='ajax-fc-border' id='ajax-fc-right' src='" + options.captchaDir + "/border-right.png' /><div id='ajax-fc-content'><div id='ajax-fc-left'><p id='ajax-fc-task'>" + options.text + "</p><ul id='ajax-fc-task'><li class='ajax-fc-0'><img src='" + options.captchaDir + "/item-none.png' alt='' /></li><li class='ajax-fc-1'><img src='" + options.captchaDir + "/item-none.png' alt='' /></li><li class='ajax-fc-2'><img src='" + options.captchaDir + "/item-none.png' alt='' /></li><li class='ajax-fc-3'><img src='" + options.captchaDir + "/item-none.png' alt='' /></li><li class='ajax-fc-4'><img src='" + options.captchaDir + "/item-none.png' alt='' /></li></ul></div><div id='ajax-fc-right'><a target='_blank' href='http://www.webdesignbeach.com'><img id='ajax-fc-backlink' src='" + options.captchaDir + "/wdb.png' alt='Web Design Beach' /></a><p id='ajax-fc-circle'></p></div></div><div id='ajax-fc-corner-spacer'></div><b class='ajax-fc-rbottom'><b class='ajax-fc-r4'></b> <b class='ajax-fc-r3'></b> <b class='ajax-fc-r2'></b> <b class='ajax-fc-r1'></b></b>");
	var rand =  jQuery.trim($.ajax({ url: options.url,async: false }).responseText);
	
	var pic = Math.ceil(Math.random()*5) - 1;
	$(".ajax-fc-" + rand).html( "<img src=\"" + options.captchaDir +"/item-" + options.items[pic] + ".png\" alt=\"\" />");
	$("p#ajax-fc-task span").html(options.items[pic]);
	$(".ajax-fc-" + rand).addClass('ajax-fc-highlighted');
	$(".ajax-fc-" + rand).draggable({ containment: '#ajax-fc-content' });
	var used = Array();
	for(var i=0;i<5;i++){
		if(i != rand && i != pic)	
		{
			$(".ajax-fc-" +i).html( "<img src=\"" + options.captchaDir +"/item-" + options.items[i] + ".png\" alt=\"\" />");
			used[i] = options.items[i];
		}
	}
	$(".ajax-fc-container, .ajax-fc-rtop *, .ajax-fc-rbottom *").css("background-color", options.borderColor);
	$("#ajax-fc-circle").droppable({
		accept: ".ajax-fc-" + rand,
		drop: function(event, ui) {
			$(".ajax-fc-" + rand).draggable("disable");
			$("#" + options.formId).append("<input type=\"hidden\" style=\"display: none;\" name=\"captcha\"  id=\"captcha\" value=\"" + rand + "\">");
		},
		tolerance: 'touch'
	});	
	};