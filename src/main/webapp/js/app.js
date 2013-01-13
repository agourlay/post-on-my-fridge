window.App = Ember.Application.create({	
    ApplicationView: Ember.View.extend(),
    
    ApplicationController: Ember.Controller.extend(),
    
    //TODO remove this ugly thing
	fridgeId : location.pathname.split('/')[2],

	ready : function() {
		document.title = "Fridge "+ this.fridgeId;
	},

    Router : Ember.Router.extend({
		enableLogging: false,
		location: 'hash',
		root: Ember.Route.extend({
			index: Ember.Route.extend({
				route: '/',
				connectOutlets: function(router, event) {
					router.get('applicationController').connectOutlet('fridgeOutlet', 'fridge');
					router.get('applicationController').connectOutlet('chatOutlet', 'chat');
					router.get('applicationController').connectOutlet('headerOutlet', 'header');
				}
			})
		})
    })
});