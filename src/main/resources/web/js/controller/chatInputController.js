App.ChatInputController = Ember.ObjectController.extend({

	sendChatMessage: function() {
		var message = this.get('content');
		this.set("content","");
		var pseudo = App.Dao.pseudo();
		var payload = {};
		payload.user = pseudo;
		payload.message = message;
		payload.timestamp = new Date().getTime();
		var controller = this;
		this.postMessage(payload).done(function(){
			App.Dao.addLocalMessage(payload);
		});
	},

	postMessage : function(payload) {
		return $.ajax({
			type: "POST",
			url: "messages/" + App.Dao.get('fridgeId') + "?token=" + App.Dao.get("userToken"),
			contentType: "application/json",
			dataType: "text",
			data: JSON.stringify(payload),
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Message not sent!");
			}
		});
	}
});