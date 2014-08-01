App.PanelView = Ember.View.extend({
	tagName: 'aside',
	elementId: 'panel',
	contentBinding: 'controller.content',
	viewers : 1,
	opened : true,

	fridgeName: function() {
 		return this.get('content');
 	}.property('content'),

	participants : function() {
		if (this.get("viewers") > 1){
			return this.get("viewers") + " participants"
		} else {
			return this.get("viewers") + " participant"
		}
	}.property('viewers'),

	toggleMode : function() {
		var view = this;
		var togglePanel = $('#togglePanel');
		if (view.get('opened')){
			view.$().hide("slide", { direction: "left" }, 300);
			togglePanel.animate({'marginLeft' : "-=220px"});
		} else {
			view.$().show("slide", { direction: "left" }, 300);
			togglePanel.animate({'marginLeft' : "+=220px"});
		}
		view.set('opened',!view.get('opened'));
	},	

	didInsertElement : function() {	
		var view = this;
		$('#togglePanel').css({ top: $(window).height() / 2 })
		                 .click(function() { view.toggleMode() });
		$.getJSON("chat/" + App.Dao.get('fridgeId') + "/participants", function(number) {
			if (number !== null) {
				view.set("viewers", number);
			}
			App.Dao.get("eventBus").onValue(function(evt){     
				if (evt.command === "participantAdded") {
					view.incrementProperty("viewers");
				}
				if (evt.command === "participantRemoved") {
					view.decrementProperty("viewers");
				}
	        });
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