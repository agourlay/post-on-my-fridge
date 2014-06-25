window.ENV = { ENABLE_ALL_FEATURES: true };

window.App = Ember.Application.createWithMixins({
  LOG_TRANSITIONS: true,
  LOG_STACKTRACE_ON_DEPRECATION: true,
  LOG_VERSION: true,
});

App.ApplicationView = Em.View.extend({
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
    window.scrollTo(0, 0);
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