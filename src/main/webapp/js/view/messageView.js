App.MessageView = Em.View.extend({
	tagName: 'span',
    templateName: 'message-template',
	didInsertElement: function() {
		$("#chatLog").animate({
			scrollTop: $("#chatLog").prop("scrollHeight")
		}, 3000);
	}
});