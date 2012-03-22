$(function() {
	initPage();
	setRandomBackGround();
	setInterval("initPage()", 10000);
});

function initPage(){
	var fridge = $('.fridge');
    generateCaptcha();
	colorPickerManagement();
	datePickerManagement();
	$.getJSON("/getPost", function(data) {
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
					url: "/remove?id="+ui.draggable.attr('id')
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
