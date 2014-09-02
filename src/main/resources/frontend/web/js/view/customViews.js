App.LazyColorFieldView = Ember.View.extend(Ember.TextSupport,{
  attributeBindings: ['value', 'type', 'size', 'name', 'placeholder', 'disabled', 'maxlength'],
  tagName: 'input',
  type: 'color',
  valueBinding: Ember.Binding.oneWay('source')
});

App.LazyTextFieldView = Ember.TextField.extend({
  classNames: ['form-control', 'input-sm'],
  valueBinding: Ember.Binding.oneWay('source')
});

App.LazyTextAreaView = Ember.TextArea.extend({
  classNames: ['form-control'],
  valueBinding: Ember.Binding.oneWay('source')
});