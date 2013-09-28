App.MessageView = Em.View.extend({
	tagName: 'span',
	templateName: 'message',
    classNames: ['chat-message'],
	didInsertElement: function() {
		var parentViewJq = this.get('parentView').$();
		parentViewJq.animate({
			scrollTop: parentViewJq.prop("scrollHeight")
		}, 200);
	}
});