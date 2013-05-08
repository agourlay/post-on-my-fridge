App.MessagesController = Ember.ArrayController.extend({
	content: [],
	
	watchContent: function() {
		console.log("MessagesController content changed : " + JSON.stringify(this.get('content')));
	}.observes('content'),
	
	init: function () {
		this.get('content').clear();
		this.retrievePreviousMessages();
		App.Dao.streamRegistering(null,this);
	},

	messageManagement: function(message) {
		var messageModel = App.Message.createWithMixins(message);
		this.pushObject(messageModel);
	},
	
	retrievePreviousMessages: function() {
		var me = this;
		$.getJSON("api/message/" + App.Dao.get('fridgeId') , function(messages) {
			if (messages !== null && messages.length !== 0) {
				$.each(messages, function(index, message) {
					me.messageManagement(message.user, message.message,message.timestamp);
				});
			}
		});
	}
});