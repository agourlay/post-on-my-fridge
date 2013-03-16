App.ColorFieldView = Ember.View.extend(Ember.TextSupport,{
  attributeBindings: ['value', 'type', 'size', 'name', 'placeholder', 'disabled', 'maxlength'],
  tagName: 'input',
  type: 'color'
});

App.DateFieldView = Ember.View.extend(Ember.TextSupport,{
  attributeBindings: ['value', 'type', 'size', 'name', 'placeholder', 'disabled', 'maxlength'],
  tagName: 'input',
  type: 'date'
});