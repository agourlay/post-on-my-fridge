App.Message = Em.Object.extend({
	user: null,
	message: null,
	date : null,
	timestamp: moment().format('HH:mm')
});