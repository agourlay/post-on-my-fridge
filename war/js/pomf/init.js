$(function() {
	setRandomBackGround();
	colorPickerManagement();
	datePickerManagement();
	generateCaptcha();
	setupSearchAutocomplete();
	konami();
	initPage();
	setInterval("initPage()", 10000);
	setTimeout(showPage, 3000);
});

function initPage(){
	var fridge = $('.fridge');
    var fridgeId = $("#fridgeId").val();
	$.getJSON("/resources/fridge/"+fridgeId, function(data) {
		if (data != undefined){
			deleteProcedure(data);
			createOrUpdate(data);
		}
	
		$( ".newPost" ).draggable({ revert: "invalid" ,scroll: true});
		
		$( ".trash_bin" ).droppable({
			accept: ".post",
			drop: function( event, ui ) {
				$(this).effect("bounce",{ times:3 }, 300);
				$.ajax({
					type:'DELETE',
					url: "/resources/post/"+ui.draggable.attr('id')
				});
				deleteAnimationPost(ui.draggable.attr('id'));
			}
		});
		
		$( ".fridge" ).droppable({
			accept: ".post, .newPost",
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
					myData ["fridgeId"] = fridgeId;
					$.ajax({
				            url: "/resources/post",
				            data : myData,
				            dataType: "html",
				            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				            type:"post",
				            complete: replaceNewPost(ui.draggable),
				            success : initPage(),
				            error:function (xhr, ajaxOptions, thrownError){
				               	$.jGrowl("Please solve the captcha!")
				             	}
			               });	
				}else{
					var myData = {};
					myData ["positionX"] = (parseInt(ui.draggable.css('left'))) / fridge.width();
					myData ["positionY"] = (parseInt(ui.draggable.css('top'))) / fridge.height();
					$.ajax({ 
						url: "/resources/post/"+ui.draggable.attr('id'),
						type: "put",
						data : myData});	
				}	
			}
		});		
	});
}