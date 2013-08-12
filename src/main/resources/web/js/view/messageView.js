App.MessageView = Em.View.extend({
	tagName: 'span',
	templateName: 'message',
    classNames: ['chat-message'],
	didInsertElement: function() {
		$("#chat-messages").animate({
			scrollTop: $("#chat-messages").prop("scrollHeight")
		}, 250);
	}
});