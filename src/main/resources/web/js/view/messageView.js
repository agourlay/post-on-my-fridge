App.MessageView = Em.View.extend({
	tagName: 'span',
	templateName: 'message',
    classNames: ['chatMessage'],
	didInsertElement: function() {
		$(".chatLog").animate({
			scrollTop: $(".chatLog").prop("scrollHeight")
		}, 250);
	}
});