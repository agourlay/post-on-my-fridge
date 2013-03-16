App.ChatController = Ember.ArrayController.extend({
	content: [],
	
	fridgeName : function(){
		return App.Dao.get('fridgeId');
	}.property().cacheable(false),	
	
	watchContent: function() {
		console.log("ChatController content changed :" + JSON.stringify(this.get('content')));
	}.observes('content'),
	
	initController: function () {
		this.get('content').clear();
		this.retrievePreviousMessages();
	},

	sendChatMessage: function(message) {
		var pseudo = App.Dao.pseudo();
		var payload = {};
		payload.fridgeName = this.get('fridgeName');
		payload.user = pseudo;
		payload.message = message;
		$.ajax({
			type: "POST",
			url: "api/message/"+this.get('fridgeName'),
			contentType: "application/json",
			dataType: "text",
			data: JSON.stringify(payload)
		});
	},

	messageManagement: function(user,message,timestamp) {
		var chatModel = {};
		chatModel.user = user;
		chatModel.message = message;
		chatModel.timestamp = timestamp;
		var messageModel = App.Message.createWithMixins(chatModel);
		this.pushObject(messageModel);
	},
	
	retrievePreviousMessages: function() {
		var me = this;
		$.getJSON("api/message/"+this.get('fridgeName') , function(messages) {
			if (messages !== null && messages.length !== 0) {
				$.each(messages, function(index, message) {
					me.messageManagement(message.user, message.message,message.timestamp);
				});
			}
		});
	}
});