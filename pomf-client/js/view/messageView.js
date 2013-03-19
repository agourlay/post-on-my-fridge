App.MessageView = Em.View.extend({
	tagName: 'span',
    templateName: 'message-template',
    classNames: ['chatMessage'],
	didInsertElement: function() {
		$("#chatLog").animate({
			scrollTop: $("#chatLog").prop("scrollHeight")
		}, 500);
	}
});