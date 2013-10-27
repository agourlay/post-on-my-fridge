App.PanelView = Ember.View.extend({
	tagName: 'aside',
	elementId: 'panel',
	contentBinding: 'controller.content',

	didInsertElement : function() {	
		var view = this;
		if(typeof store.get('username') !== "undefined"){
			$("#pseudo").val(store.get('username'));
		}
		
		$("#pseudo").focusout( function(){
			if ($("#pseudo").val() !== store.get('username')){
				store.set('username', $("#pseudo").val() !== "" ? $("#pseudo").val() : "Anonymous");
				App.Dao.renameParticipant(store.get('username'));
			}
		});
	}
});