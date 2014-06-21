App.FridgesController = Ember.ArrayController.extend({
	sortProperties: ['modificationDate'],
	sortAscending: false,
	page: 1,

 	actions: {  
 		createFridge : function(name){
			var me = this;
			App.Dao.createFridge(name).then(function (fridge) {
				me.transitionToRoute('fridge', App.Dao.initSessionData(fridge.id));	
			});	
		},

 		nextPage : function() {
 			var currentPage = parseInt(this.get("page"));
			var me = this;
			App.Dao.getFridges(currentPage + 1).then(function (fridges) {
				me.get("content").clear();
			    me.get("content").pushObjects(fridges);
			    me.set("page", currentPage + 1);
			});
		},

		previousPage : function() {
			var currentPage = parseInt(this.get("page"));
			if (currentPage != 1){
				var me = this;
				App.Dao.getFridges(currentPage - 1).then(function (fridges) {
					me.get("content").clear();
			    	me.get("content").pushObjects(fridges);
			    	me.set("page", currentPage - 1);
				});
			}
		}
 	}
});