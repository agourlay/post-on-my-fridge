window.App = Ember.Application.create({	
    ApplicationView: Ember.View.extend(),
    
    ApplicationController: Ember.Controller.extend(),
    
	fridgeId : location.pathname.split('/')[2],

	ready : function() {
		document.title = this.fridgeId + " 's fridge";
	},

    Router : Ember.Router.extend({
		enableLogging: true,
		location: 'hash',
		root: Ember.Route.extend({
			index: Ember.Route.extend({
				route: '/',
				connectOutlets: function(router, event) {
					router.get('applicationController').connectOutlet('fridgeOutlet','fridge');
					router.get('applicationController').connectOutlet('chatOutlet','chat');
					router.get('applicationController').connectOutlet('headerOutlet','header');
				}
			})
		})
    })
});