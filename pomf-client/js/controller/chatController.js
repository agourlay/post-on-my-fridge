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
		this.streamManagement();
		this.retrievePreviousMessages();
	},

	sendChatMessage: function(message, pseudo) {
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

	streamManagement : function () {
		var me = this;
		var source = new EventSource("stream/" + this.get('fridgeName'));
		source.addEventListener('message', function(e) {
			console.log(e.data);
			var data = $.parseJSON(e.data);
			if (data.command === "refresh") {
				App.Dao.refresh();
			}
			if (data.command === "message") {
				me.messageManagement(data.user, data.message,data.timestamp);
			}
		}, false);

		source.addEventListener('open', function(e) {
			console.log("SSE opened!")
		}, false);

		source.addEventListener('error', function(e) {
			if (e.readyState == EventSource.CLOSED) {
			    errorMessage("Channel error");
			}
		}, false);
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