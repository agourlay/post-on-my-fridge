App.MessagesController = Ember.ArrayController.extend({
	content: [],
		
	init: function () {
		App.Dao.streamRegistering(null, this);
		this.initData();
	},

	reload : function (){
		this.get('content').clear();
		this.initData();
	},

	initData : function() {
		var me = this;
		this.joinChat().done(function(){
			me.retrieveParticipantNumber();
		});
		this.retrievePreviousMessages();
	},

	willDestroy : function (){
		alert("will destroy");
	},

	messageManagement: function(message) {
		var messageModel = App.Message.createWithMixins(message);
		this.pushObject(messageModel);
	},

	notificationManagement: function(command,name, timestamp) {
		var messageModel = App.Message.create();
		if (command === "participantAdded") {
			messageModel.set("user" ,name);
		    messageModel.set("message" ,"joined the chat");
		    messageModel.set("timestamp" ,timestamp);
		}

		if (command === "participantRemoved") {
			messageModel.set("user" ,name);
		    messageModel.set("message" ,"left the chat");
		    messageModel.set("timestamp" ,timestamp);
		}

		if (command === "participantRenamed") {
			messageModel.set("user" ,name);
		    messageModel.set("message" ,"changed his name");
		    messageModel.set("timestamp" ,timestamp);
		}
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
				//infoMessage(""+number);
			}
		});
	},

	joinChat: function() {
		return $.ajax({
			url: "chat/" + App.Dao.get('fridgeId') + "/participants?token=" + App.Dao.get("userToken"),
			method: "POST",
        	contentType: "application/json",
        	dataType: "text",
        	data: JSON.stringify(App.Dao.pseudo()),
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Could not join chat");
			}
		});
	},

	renameParticipant: function(name) {
		return $.ajax({
			url: "chat/" + App.Dao.get('fridgeId') + "/participants?token=" + App.Dao.get("userToken"),
			method: "PUT",
        	contentType: "application/json",
        	dataType: "text",
        	data: JSON.stringify(name),
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Could not join chat");
			}
		});
	}
});
