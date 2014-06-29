App.MessagesController = Ember.ArrayController.extend({

	init: function () {
		var me = this; 
		App.Dao.set("messagesController", this);
		App.Dao.get("eventBus").onValue(function(evt){
			var payload = evt.payload;
			var timestamp = evt.timestamp;
			var cmd = evt.command
            if (cmd === "messageSent") {
				me.messageManagement(payload);
			}
			if (cmd === "participantAdded" || cmd === "participantRemoved" || cmd === "participantRenamed") {
				me.notificationManagement(payload, timestamp);
			}
        });
		this.initData();
	},

	reload : function (){
		this.get('content').clear();
		this.initData();
	},

	initData : function() {
		var me = this;
		me.joinChat().done(function(){
			me.retrievePreviousMessages();
			me.retrieveParticipantNumber();
		});
	},

	messageManagement: function(message) {
		var messageModel = App.Message.create();
		messageModel.set("message" ,message.message);
		messageModel.set("user" ,message.user);
		messageModel.set("timestamp" ,message.timestamp);
		messageModel.set("isNotification" ,false);
		this.pushObject(messageModel);
	},

	notificationManagement: function(message, timestamp) {
		var messageModel = App.Message.create();
		messageModel.set("message" ,message);
		messageModel.set("timestamp" ,timestamp);
		messageModel.set("isNotification" ,true);
		this.pushObject(messageModel);
	},
	
	retrievePreviousMessages: function() {
		var me = this;
		$.getJSON("chat/" + App.Dao.get('fridgeId') + "/messages", function(messages) {
			if (messages !== null && messages.length !== 0) {
				$.each(messages, function(index, message) {
					me.messageManagement(message);
				});
			}
		});
	},

	retrieveParticipantNumber: function() {
		var me = this;
		$.getJSON("chat/" + App.Dao.get('fridgeId') + "/participants", function(number) {
			if (number !== null) {
				if (number === 1){
					me.notificationManagement("You are alone in this chat", moment().format("YYYY-MM-DDTHH:mm:ssZZ"));
				}else{
					me.notificationManagement(number + " participants online", moment().format("YYYY-MM-DDTHH:mm:ssZZ"));
				}
			}
		});
	},

	joinChat: function() {
		return $.ajax({
			url: "chat/" + App.Dao.get('fridgeId') + "/participants?token=" + App.Dao.get("userToken"),
			method: "POST",
        	contentType: "application/json",
        	dataType: "text",
        	data: App.Dao.pseudo(),
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Could not join chat");
			}
		});
	},

	leaveChat: function(chatName) {
		return $.ajax({
			url: "chat/" + chatName + "/participants?token=" + App.Dao.get("userToken"),
			method: "DELETE",
			async : false,
			timeout : 1000,
        	contentType: "application/json",
        	dataType: "text",
        	data: App.Dao.pseudo(),
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Could not exit chat");
			}
		});
	},

	renameParticipant: function(name) {
		return $.ajax({
			url: "chat/" + App.Dao.get('fridgeId') + "/participants?token=" + App.Dao.get("userToken"),
			method: "PUT",
        	contentType: "application/json",
        	dataType: "text",
        	data: name,
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Could not rename participant");
			}
		});
	}
});
