App.Message = Em.Object.extend({
	user: null,
	message: null,
	timestamp: null,
	isNotification: false,
	date: function (){
		return moment(this.timestamp, "YYYY-MM-DDTHH:mm:ssZZ").format('HH:mm');
	}.property()
});