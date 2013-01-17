App.ChatView = Ember.CollectionView.extend({
	contentBinding: 'App.ChatController',
	tagName: 'div',
	elementId: 'chatLog',
	itemViewClass: 'App.MessageView',
	pseudo: function() {
		if ($("#pseudo").val() !== ""){
			var inputName = $("#pseudo").val();
			store.set('username', inputName);
			return inputName;
		}

		if(typeof store.get('username') !== "undefined"){
			var pseudoStored = store.get('username');
			$("#pseudo").val(pseudoStored);
			return pseudoStored;
		}
		
		return "Anonymous";	
	}.property().cacheable(false),
		
	didInsertElement: function() {
		var view = this;
		view.get('pseudo');
		var handleReturnKey = function(e) {
				if(e.charCode === 13 || e.keyCode === 13) {
					e.preventDefault();
					App.ChatController.sendChatMessage($("#message").val(), view.get('pseudo'));
					$("#message").val('');
				}
			};
		$("#message").keypress(handleReturnKey);
	}
});