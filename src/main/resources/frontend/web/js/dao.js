App.Dao = Em.Object.create({

	fridgeId : null,
	source : null,
	messagesController : null,
	postsController : null,
	userToken: null,
	eventBus:null,

	initSessionData : function(fridgeId) {
		var me = this;
		return me.findFridgeById(fridgeId).then(function (fridge) {
    		me.set('fridgeId',fridge.id);
			me.set('eventBus', new Bacon.Bus())
			if(me.get("userToken") == null){
				me.retrieveUserToken().then(function(token){
					me.streamManagement(fridge.id, token);
				});
			}
			if (me.get("messagesController") != undefined){
				me.get("messagesController").reload();
			}
			return fridge;
        });
	},

	addDefaultPost : function (){
		var newPostData = {};
		newPostData.author = App.Dao.pseudo();
		newPostData.content = "new post";
		newPostData.color = randomColor();
		newPostData.positionX = getRandomPostInitX();
		newPostData.positionY = getRandomPostInitY();
		newPostData.fridgeId = App.Dao.get('fridgeId');
		newPostValidation(newPostData);
		this.get('postsController').createPost(newPostData);
	},

	addLocalMessage : function (message) {
		this.get('messagesController').messageManagement(message);
	},

	streamManagement : function (fridgeId, token) {
		var me = this;
		me.set("source", new EventSource("stream/fridge/" + fridgeId +"?token="+ token));
		var source = me.get("source");
		source.addEventListener('message', function(e) {
			var data = $.parseJSON(e.data);
			var payload = data.payload;
			var timestamp = data.timestamp;
			me.get("eventBus").push(data);
		}, false);

		source.addEventListener('open', function(e) {
			console.log("SSE opened!")
		}, false);

		source.addEventListener('error', function(e) {
			if (e.readyState == EventSource.CLOSED) {
			    errorMessage("Streaming service error");
			}
		}, false);
	},

	pseudo : function() {
		if(typeof store.get('username') == "undefined"){
			store.set('username', "Anonymous");
		}
		return store.get('username');	
	},

	leaveChatOnExit : function () {
		if (this.get('fridgeId') != null) {
		    this.get("messagesController").leaveChat(this.get('fridgeId'));
		}
	},

	renameParticipant : function (newName) {
		this.get("messagesController").renameParticipant(newName);
	},

	findFridgeById : function(fridgeId) {
		return $.ajax({
        	url: "fridges/" + fridgeId,
        	type: 'GET',
        	error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during fridge retrieval");
			}
    	}).then(function (fridge) {
    		var model = App.Fridge.create();
		    model.set('id', fridgeId);
            var subsModel = Ember.A([]);
            if (fridge !== null && fridge !== undefined) {
				model.set('name', fridge.name);
				model.set('id', fridge.id);
				model.set('creationDate', fridge.creationDate);
				model.set('modificationDate', fridge.modificationDate);
				model.set('posts', fridge.posts.map(function(post){ return App.Post.createWithMixins(post); }));
			}
            return model;
        });
	},

	retrieveUserToken : function() {
		var me = this;
		return $.ajax({
	        url: "token",
	        type: 'GET',
	        error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during token retrieval");
			}
    	}).then(function (token){
    		me.set('userToken',token);
    		return token;
    	});
	},

	getStats : function () {
		var idxModel = App.Index.create();
		var fridgeCountDefer = $.ajax({
	        url: "count/fridges",
	        type: 'GET',
	        success: function(nbfridge) {
				if (nbfridge !== null && nbfridge !== undefined) {
					idxModel.set('nbFridges', nbfridge);
				}
	        },
	        error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during count fridges retrieval");
			}
    	});

		var postCountDefer = $.ajax({
        	url: "count/posts",
        	type: 'GET',
        	success: function(nbpost) {
				if (nbpost !== null && nbpost !== undefined) {
					idxModel.set('nbPosts', nbpost);
				}
        	},
        	error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during count posts retrieval");
			}
		});

		return idxModel;
	},

	createFridge: function(name) {
		return $.ajax({
			url: "fridges",
			method: "POST",
        	contentType: "application/json",
        	data: name,
			error: function(xhr, ajaxOptions, thrownError) {
				errorMessage("Error during fridge creation");
			}
		});
	},

	getFridges : function () {
		return $.ajax({
	        	url: "fridges",
	        	type: 'GET',
	        	error: function(xhr, ajaxOptions, thrownError) {
					errorMessage("Error during fridges retrieval");					
				}
    		}).then(function (fridges) {
            	var fridgesModel = Ember.A([]);
            	$.each(fridges, function(i, fridge){
					var fridge = App.Fridge.create({
						id:fridge.id,
						name:fridge.name,
						creationDate:fridge.creationDate,
						modificationDate:fridge.modificationDate,
						postNumber:fridge.postNumber,
						posts: fridge.posts.map(function(post){ return App.Post.createWithMixins(post); })
					});
					fridgesModel.pushObject(fridge);	
				});	
            	return fridgesModel;
        	});
	}, 

	metrics : function()  {
        var dao = this;
        return $.ajax({
            url: "stats",
            type: 'GET',
            error: function(xhr, ajaxOptions, thrownError) {
                console.log("Error during current system stat retrieval");                                        
            }
        }).then(function (json) {
            var system = App.Metrics.create();
            var meters = dao.buildMetric(json, 5);
            var timers = dao.buildMetric(json, 15);
            var counters = dao.buildMetric(json, 1);
            system.set('meters', meters);
            system.set('timers', timers);
            system.set('counters', counters);
            return system;
        });
    },

    buildMetric : function (json, keyNb) {
        var filtered = this.filterByFieldNumber(json, keyNb);
        var metrics = [];
        jQuery.each(filtered, function(i, val) {
            var newMetric = new Object();
            if( keyNb == 15) { newMetric = App.Timer.create(val.value); }  
            if( keyNb == 5) { newMetric = App.Meter.create(val.value); }
            if( keyNb == 1) { newMetric = App.Counter.create(val.value); }  
            newMetric.name = val.name.replace("pomf.domain.", "")
                                     .replace("pomf.service.", "")
                                     .replace("pomf.api.", "");
            metrics.push(newMetric);
        });
        return metrics.sort(function(a, b){return (a.name < b.name)?-1:1});
    },

    filterByFieldNumber : function (jsonObj, n) {
        var filtered = [];
        jQuery.each(jsonObj, function(i, val) {
            if(Object.keys(val).length == n) {
                var container = new Object();
                container.name = i;
                container.value = val;
                filtered.push(container);
            }
        });
        return filtered;
    }
});