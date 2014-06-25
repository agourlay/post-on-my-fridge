App.ChatInputView = Ember.TextArea.extend({
	attributeBindings: ['value', 'type', 'size', 'name', 'placeholder', 'disabled', 'maxlength'],	
	maxlength : "200",
	elementId : "chat-input",
	valueBinding: 'controller.content',
	classNames: ['form-control'],
	placeholder : "Chat...",

	insertNewline: function() {
		this.get('controller').sendChatMessage();
	}
});