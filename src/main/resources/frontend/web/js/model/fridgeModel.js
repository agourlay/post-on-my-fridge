App.Fridge = Em.Object.extend({
	id : null,
	name : null,
	postNumber: null,
	posts: [],
	creationDate: null,
	modificationDate: null,

	prettyCreationDate: function(){
		return moment(this.creationDate, "YYYY-MM-DDTHH:mm:ssZZ").format("dddd, MMMM Do YYYY");
	}.property("creationDate"),

	relativeLastUpdate: function(){
		return moment(this.modificationDate, "YYYY-MM-DDTHH:mm:ssZZ").fromNow();
	}.property("modificationDate")
});