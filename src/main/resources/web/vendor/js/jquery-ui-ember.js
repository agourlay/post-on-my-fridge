// Create a new mixin for jQuery UI widgets using the new SproutCore 2.0
// mixin syntax.
App.Base = Ember.Mixin.create({
  // When SproutCore creates the view's DOM element, it will call this
  // method.
  didInsertElement: function() {
    this._super();

    // Make jQuery UI options available as SproutCore properties
    var options = this._gatherOptions();

    // Make sure that jQuery UI events trigger methods on this view.
    this._gatherEvents(options);

    // Create a new instance of the jQuery UI widget based on its `uiType`
    // and the current element.
    var ui = jQuery.ui[this.get('uiType')](options, this.get('element'));

    // Save off the instance of the jQuery UI widget as the `ui` property
    // on this SproutCore view.
    this.set('ui', ui);


  },

  // When SproutCore tears down the view's DOM element, it will call
  // this method.
  willDestroyElement: function() {
    var ui = this.get('ui');

    if (ui) {
      // Tear down any observers that were created to make jQuery UI
      // options available as SproutCore properties.
      var observers = this._observers;
      for (var prop in observers) {
        if (observers.hasOwnProperty(prop)) {
          this.removeObserver(prop, observers[prop]);
        }
      }
      ui._destroy();
    }
  },

  // Each jQuery UI widget has a series of options that can be configured.
  // For instance, to disable a button, you call
  // `button.options('disabled', true)` in jQuery UI. To make this compatible
  // with SproutCore bindings, any time the SproutCore property for a
  // given jQuery UI option changes, we update the jQuery UI widget.
  _gatherOptions: function() {
    var uiOptions = this.get('uiOptions'),
      options = {};

    // The view can specify a list of jQuery UI options that should be treated
    // as SproutCore properties.
    uiOptions.forEach(function(key) {
      options[key] = this.get(key);

      // Set up an observer on the SproutCore property. When it changes,
      // call jQuery UI's `setOption` method to reflect the property onto
      // the jQuery UI widget.
      var observer = function() {
          var value = this.get(key);
          this.get('ui')._setOption(key, value);
        };

      this.addObserver(key, observer);

      // Insert the observer in a Hash so we can remove it later.
      this._observers = this._observers || {};
      this._observers[key] = observer;
    }, this);

    return options;
  },

  // Each jQuery UI widget has a number of custom events that they can
  // trigger. For instance, the progressbar widget triggers a `complete`
  // event when the progress bar finishes. Make these events behave like
  // normal SproutCore events. For instance, a subclass of JQ.ProgressBar
  // could implement the `complete` method to be notified when the jQuery
  // UI widget triggered the event.
  _gatherEvents: function(options) {
    var uiEvents = this.get('uiEvents') || [],
      self = this;

    uiEvents.forEach(function(event) {
      var callback = self[event];

      if (callback) {
        // You can register a handler for a jQuery UI event by passing
        // it in along with the creation options. Update the options hash
        // to include any event callbacks.
        options[event] = function(event, ui) {
          callback.call(self, event, ui);
        };
      }
    });
  }
});


App.Draggable = Ember.Mixin.create(App.Base, {
  uiType: 'draggable',
  uiOptions: ['disabled', 'addClasses', 'appendTo', 'axis', 'cancel', 'connectToSortable', 'containment', 'cursor', 'delay', 'distance', 'grid', 'handle', 'snap', 'snapMode', 'stack'],
  uiEvents: ['create', 'start', 'drag', 'dragstop']
});