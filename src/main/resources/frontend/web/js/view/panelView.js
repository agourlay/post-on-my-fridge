App.PanelView = Ember.View.extend({
	tagName: 'aside',
	elementId: 'panel',
	contentBinding: 'controller.content',
	viewers : 1,

	fridgeName: function() {
		return this.get('content');
	}.property('content'),

	rssUrl: function() {
		var fridgeName = this.get('content');
		document.title = "Fridge "+ fridgeName;
		return "/rss/fridge/" + fridgeName;
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