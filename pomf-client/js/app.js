window.App = Ember.Application.createWithMixins({
	LOG_TRANSITIONS: true,
	LOG_BINDINGS : true
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