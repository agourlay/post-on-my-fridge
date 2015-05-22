window.App = Ember.Application.createWithMixins({
  LOG_STACKTRACE_ON_DEPRECATION: true
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
  }.observes('currentPath')
});