App.Message = Em.Object.extend({
	user: null,
	message: null,
	timestamp: null,
	isNotification: false,
	date: function (){
		return moment(new Date(this.get('timestamp'))).format('HH:mm');
	}.property()
});