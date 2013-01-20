App.Router.map(function() {
    this.resource('index', {path: '/fridge'});
});

App.IndexRoute = Ember.Route.extend({
    renderTemplate: function(controller, model) {
        this.render('fridge', {outlet: 'fridge'});
        this.render('chat', {outlet: 'chat'});
        this.render('header', {outlet: 'header'});
    }
});