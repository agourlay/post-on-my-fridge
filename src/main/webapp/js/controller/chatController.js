App.ChatController = Ember.ArrayController.create({
	content: [],
	chatSocket : null,

	init: function () {
		this._super();
		this.channelManagement();
		this.retrievePreviousMessages();
	},

	sendChatMessage: function(message,pseudo){
		var payload = {};
		payload.fridgeId = App.get('fridgeId');
		payload.message = message;
		payload.user = pseudo;
		$.ajax({
			type: "POST",
			url: "/_ah/channel/" +  App.get('fridgeId') + "/message",
			data: payload
		});
	},

	messageManagement: function(user, message) {
		var chatModel = {};
		chatModel.user = user;
		chatModel.message = message;
		chatModel.timestamp = moment().format('HH:mm');
		this.pushObject(App.Message.create(chatModel));
	},

	channelManagement : function () {
		var me = this;
		$.ajax({
			url:"/_ah/channel/" + App.get('fridgeId'),
			type: "GET",
			dataType:'text',
			success: function(tokenChannel) {
				if (tokenChannel !== undefined) {
					var channel = new goog.appengine.Channel(tokenChannel);
					var socket = channel.open();
					socket.onopen = function() {};
					socket.onclose = function() {};
					socket.onmessage = function(m) {
						var data = $.parseJSON(m.data);
						if (data.command == "#FRIDGE-UPATE#") {
							App.FridgeController.retrievePost();
						}
						if (data.command == "#FRIDGE-CHAT#") {
							me.messageManagement(data.user, data.message);
						}
					};
					socket.onerror = function(err) {
						jackedup = humane.create({
							baseCls: 'humane-jackedup',
							addnCls: 'humane-jackedup-error'
						});
						jackedup.log("Channel error :" + err.description);
					};
				}
			}	
		});
	},
	
	retrievePreviousMessages: function() {
		var me = this;
		$.getJSON("/_ah/channel/" + App.get('fridgeId') + "/message", function(messages) {
			if (messages !== null) {
				$.each(messages, function(index, message) {
					me.messageManagement(message.user,message.message,message.date);
				});
			}
		});
	}
});