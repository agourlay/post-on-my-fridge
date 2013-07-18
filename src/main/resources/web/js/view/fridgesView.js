 App.FridgesView = Em.View.extend({
    tagName : 'div',
	classNames: ['global'],
	contentBinding: 'controller.content',  
   
    rssUrl:  "/rss/fridges";
    hearderTitle : "Explore all the fridges";

    didInsertElement : function() {
		var view = this;
		document.title = "All the fridges;
	}
 });    