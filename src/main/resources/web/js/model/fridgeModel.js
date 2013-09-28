App.Fridge = Em.Object.extend({
	id : null,
	name : null,
	description : null,
	posts: [],
	loaded: false,

	postNumber: function(){
		return this.posts.length ;
	}.property("loaded"),
});