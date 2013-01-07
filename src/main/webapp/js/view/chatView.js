App.ChatView = Ember.CollectionView.extend({
	contentBinding: 'App.ChatController',
	tagName: 'div',
	elementId: 'chatLog',
	itemViewClass: 'App.MessageView',
	didInsertElement: function() {
		var handleReturnKey = function(e) {
				if(e.charCode === 13 || e.keyCode === 13) {
					e.preventDefault();
					App.ChatController.sendChatMessage($("#message").val(), $("#pseudo").val());
					$("#message").val('');
				}
			};
		$("#message").keypress(handleReturnKey);
	}
});