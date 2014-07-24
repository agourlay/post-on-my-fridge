App.ChatInputController = Ember.ObjectController.extend({
	needs: "messages",
	
	sendChatMessage: function() {
		var message = this.get('content');
		this.set("content","");
		if (message.trim()){
			var pseudo = App.Dao.pseudo();
			var payload = {};
			payload.user = pseudo;
			payload.message = message;
			payload.timestamp = moment().format("YYYY-MM-DDTHH:mm:ssZZ");
			var controller = this;
			this.postMessage(payload).done(function(){
				controller.get("controllers.messages").messageManagement(payload);
			});
		}
	},

	postMessage : function(payload) {
		return $.ajax({
			type: "POST",
			url: "chat/" + App.Dao.get('fridgeId') + "/messages?token=" + App.Dao.get("userToken"),
			contentType: "application/json",
			dataType: "text",
			data: JSON.stringify(payload),
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Message not sent!");
			}
		});
	}
});