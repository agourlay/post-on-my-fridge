App.ChatView = Ember.CollectionView.extend({
	tagName: 'div',
	classNames: ['chatLog'],
	itemViewClass: 'App.MessageView',
	contentBinding: 'controller.content',
			
	didInsertElement: function() {
		var view = this;
		var handleReturnKey = function(e) {
				if(e.charCode === 13 || e.keyCode === 13) {
					e.preventDefault();
					view.get('controller').sendChatMessage($("#chatInput").val());
					$("#chatInput").val('');
				}
			};
		$("#chatInput").keypress(handleReturnKey);
	}
});