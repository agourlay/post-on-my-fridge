App.PanelView = Ember.View.extend({
	tagName: 'aside',
	elementId: 'panel',
	contentBinding: 'controller.content',
	viewers : 1,

	fridgeName: function() {
		return this.get('content');
	}.property('content'),

	participants : function() {
		if (this.get("viewers") > 1){
			return this.get("viewers") + " participants"
		}else{
			return this.get("viewers") + " participant"
		}
	}.property('viewers'),

	didInsertElement : function() {	
		var view = this;

		$.getJSON("chat/" + App.Dao.get('fridgeId') + "/participants", function(number) {
			if (number !== null) {
				view.set("viewers", number);
			}
		});

		App.Dao.get("eventBus").onValue(function(evt){     
			if (evt.command === "participantAdded") {
				view.set("viewers", view.get("viewers") + 1 );
			}
			if (evt.command === "participantRemoved") {
				view.set("viewers", view.get("viewers") - 1 );
			}
        });

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