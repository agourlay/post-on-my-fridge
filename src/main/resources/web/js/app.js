window.App = Ember.Application.createWithMixins({
    
  debugMode: false,
  LOG_BINDINGS: false,
  LOG_VIEW_LOOKUPS: false,
  LOG_TRANSITIONS: true,
  LOG_STACKTRACE_ON_DEPRECATION: true,
  LOG_VERSION: true,
     
  templates: [
    'application',
    'index',
    'footer',
    'header',
    'fridges',
    'fridge',
    'panel/_header',
    'panel/_chat',
    'panel',
    'message',
    'trends',
    'post/_header',
    'post/_form',
    'post'
  ],

  loadTemplates: function() {
    var app = this,
        templates = this.get('templates');

    app.deferReadiness();

    var promises = templates.map(function(name) {
      return Ember.$.get('/js/templates/'+name+'.hbs').then(function(data) {
        Ember.TEMPLATES[name] = Ember.Handlebars.compile(data);
      });
    });

    Ember.RSVP.all(promises).then(function() {
      app.advanceReadiness();
    });
  },

  init: function() {
      this._super();
      this.loadTemplates();
  }
});

App.ApplicationView = Em.View.extend({
  tagName : 'div',
  elementId: 'app'
});

App.ApplicationController = Ember.Controller.extend({
  currentPathChanged: function() {
    var page;

    // Track the page in Google Analytics
    if (!Ember.isNone(_gaq)) {
      page = window.location.hash.length > 0 ?
             window.location.hash.substring(1) :
             window.location.pathname;
      _gaq.push(['_trackPageview', page]);
      _gaq.push(['_setAccount', 'UA-25345034-1']);
      _gaq.push(['_setSiteSpeedSampleRate', 100]);
    }
  }.observes('currentPath')
});