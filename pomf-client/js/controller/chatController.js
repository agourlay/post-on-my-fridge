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
		var controller = this;
		var pseudo = App.Dao.pseudo();
		var payload = {};
		payload.user = pseudo;
		payload.message = message;
		payload.timestamp = new Date().getTime();
		var promiseCreation = this.postMessage(payload);
		promiseCreation.done(function(){
			controller.messageManagement(payload);
		});
	},

	postMessage : function(payload) {
		return $.ajax({
			type: "POST",
			url: "api/message/"+this.get('fridgeName')+"?token="+ App.Dao.get("userToken"),
			contentType: "application/json",
			dataType: "text",
			data: JSON.stringify(payload),
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Message not sent!");
			}
		});
	},

	messageManagement: function(message) {
		var messageModel = App.Message.createWithMixins(message);
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