App.ChatController = Ember.ArrayController.extend({
	content: [],
	chatSocket : null,

	init: function () {
		this._super();
		this.channelManagement();
		this.retrievePreviousMessages();
	},

	sendChatMessage: function(message, pseudo) {
		var payload = {};
		payload.fridgeId = App.get('fridgeId');
		payload.message = message;
		payload.user = pseudo;
		$.ajax({
			type: "POST",
			url: "/channel/" + App.get('fridgeId') + "/message",
			data: payload
		});
	},

	messageManagement: function(user, message,timestamp) {
		var chatModel = {};
		chatModel.user = user;
		chatModel.message = message;
		chatModel.timestamp = timestamp;
		this.pushObject(App.Message.createWithMixins(chatModel));
	},

	channelManagement : function () {
		var me = this;
		$.ajax({
			url: "/channel/" + App.get('fridgeId'),
			type: "GET",
			dataType: 'text',
			success: function(tokenChannel) {
				if (tokenChannel !== undefined) {
					var channel = new goog.appengine.Channel(tokenChannel),
					    socket = channel.open();
					socket.onopen = function() {};
					socket.onclose = function() {};
					socket.onmessage = function(m) {
						var data = $.parseJSON(m.data);
						if (data.command === "#FRIDGE-UPATE#") {
							App.Dao.refresh();
						}
						if (data.command === "#FRIDGE-CHAT#") {
							me.messageManagement(data.user, data.message,data.timestamp);
						}
					};
					socket.onerror = function(err) {
						errorMessage("Channel error");
					};
				}
			}	
		});
	},
	
	retrievePreviousMessages: function() {
		var me = this;
		$.getJSON("/channel/" + App.get('fridgeId') + "/message", function(messages) {
			if (messages !== null && messages.length !== 0) {
				$.each(messages, function(index, message) {
					me.messageManagement(message.user,message.message,message.timestamp);
				});
			}
		});
	}
});