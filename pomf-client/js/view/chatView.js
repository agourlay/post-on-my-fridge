App.ChatView = Ember.CollectionView.extend({
	tagName: 'div',
	classNames: ['chatLog'],
	itemViewClass: 'App.MessageView',
	contentBinding: 'controller.content',
	
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
					view.get('controller').sendChatMessage($("#message").val(), view.get('pseudo'));
					$("#message").val('');
				}
			};
		$("#message").keypress(handleReturnKey);
	}
});