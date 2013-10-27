App.Fridge = Em.Object.extend({
	id : null,
	name : null,
	posts: [],
	loaded: false,
	creationDate: null,
	modificationDate: null,

	postNumber: function(){
		return this.posts.length ;
	}.property("loaded"),

	prettyCreationDate: function(){
		return moment(this.creationDate, "YYYY-MM-DDTHH:mm:ssZZ").format("dddd, MMMM Do YYYY");
	}.property("loaded","creationDate"),

	relativeLastUpdate: function(){
		return moment(this.modificationDate, "YYYY-MM-DDTHH:mm:ssZZ").fromNow();
	}.property("loaded","modificationDate"),
});