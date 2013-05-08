App.LazyColorFieldView = Ember.View.extend(Ember.TextSupport,{
  attributeBindings: ['value', 'type', 'size', 'name', 'placeholder', 'disabled', 'maxlength'],
  tagName: 'input',
  type: 'color',
  valueBinding: Ember.Binding.oneWay('source')
});

App.LazyDateFieldView = Ember.View.extend(Ember.TextSupport,{
  attributeBindings: ['value', 'type', 'size', 'name', 'placeholder', 'disabled', 'maxlength'],
  tagName: 'input',
  type: 'date',
  valueBinding: Ember.Binding.oneWay('source')
});

App.LazyTextField = Ember.TextField.extend({
  valueBinding: Ember.Binding.oneWay('source')
});

App.LazyTextArea = Ember.TextArea.extend({
  valueBinding: Ember.Binding.oneWay('source')
});

App.ChatTextArea = Ember.TextArea.extend({
  didInsertElement: function() {
    var view = this;
    var handleReturnKey = function(e) {
        if(e.charCode === 13 || e.keyCode === 13) {
          e.preventDefault();
          view.get('controller').sendChatMessage();
        }
      };
    view.$().keypress(handleReturnKey);  
  }
});