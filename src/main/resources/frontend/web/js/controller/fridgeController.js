App.FridgeController = Ember.ObjectController.extend({
	updateDescription : function (fridgeId, description) {
		App.Dao.updateDescription(fridgeId,description);
	}
});