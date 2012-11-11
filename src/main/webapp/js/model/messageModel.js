App.Message = Em.Object.extend({
	user: null,
	message: null,
	members: null,
	kind: null,
	timestamp: moment().format('HH:mm')
});