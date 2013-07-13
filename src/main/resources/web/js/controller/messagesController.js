App.MessagesController = Ember.ArrayController.extend({
	content: [],
		
	init: function () {
		App.Dao.streamRegistering(null,this);
		this.reload();
	},

	reload : function (){
		this.get('content').clear();
		this.retrievePreviousMessages();
	},

	messageManagement: function(message) {
		var messageModel = App.Message.createWithMixins(message);
		this.pushObject(messageModel);
	},
	
	retrievePreviousMessages: function() {
		var me = this;
		$.getJSON("api/messages/" + App.Dao.get('fridgeId') , function(messages) {
			if (messages !== null && messages.length !== 0) {
				$.each(messages, function(index, message) {
					me.messageManagement(message.user, message.message,message.timestamp);
				});
			}
		});
	}
});
