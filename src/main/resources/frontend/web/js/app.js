window.App = Ember.Application.createWithMixins({
    
  debugMode: false,
  LOG_BINDINGS: false,
  LOG_VIEW_LOOKUPS: false,
  LOG_TRANSITIONS: true,
  LOG_STACKTRACE_ON_DEPRECATION: true,
  LOG_VERSION: true,

  init: function() {
      this._super();
  }

});

App.ApplicationView = Em.View.extend({
  templateName: 'application',
  tagName : 'div',
  elementId: 'app'
});

App.ApplicationController = Ember.Controller.extend({

  init: function() {
    $(window).bind('beforeunload', function(e) {
      App.Dao.leaveChatOnExit();
    });
  },

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