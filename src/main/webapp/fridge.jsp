<!DOCTYPE html>
<html lang="en">
<head>
	<!-- META -->
	<meta name="description" content="This is an interactive fridge">
	<meta name="keywords" content="post on my fridge,social network,messaging,multimedia,fridge,post,interactive,open source" />
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<!-- CSS -->
	<link rel="stylesheet" href="/css/fridge.min.css"/>
	<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/themes/ui-lightness/jquery-ui.css"/>
	<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/humane-js/3.0.5/themes/jackedup.css"/>
	<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/humane-js/3.0.5/themes/libnotify.css"/>
	<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/jquery-powertip/1.1.0/jquery.powertip.css"/>
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,600,700,800,300' rel='stylesheet'>

	<!-- JS -->
	<script type="text/javascript">
	  	var _gaq = _gaq || [];
	  	_gaq.push(['_setAccount', 'UA-25345034-1']);
	  	_gaq.push(['_trackPageview']);
	   
	  	(function() {
	    	var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    	ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    	var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  	})();
		</script>
	<script src="/_ah/channel/jsapi"></script>
</head>
<body>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jquery-url-parser/2.2.1/purl.min.js"></script>
	<script src = "//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script src = "//ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/jquery-ui.min.js"></script>
    <script src = "//cdnjs.cloudflare.com/ajax/libs/handlebars.js/1.0.rc.1/handlebars.min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/ember.js/1.0.0-pre.2/ember-1.0.0-pre.2.min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/moment.js/1.7.2/moment.min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.4.2/underscore-min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/spin.js/1.2.7/spin.min.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/humane-js/3.0.5/humane-3.0.5.min.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jquery-powertip/1.1.0/jquery.powertip-1.1.0.min.js"></script>
	<script src = "/js/fridge.min.js"></script>
	
	<script type="text/x-handlebars" data-template-name="application">
        <div id="global">
            <header>
                {{outlet headerOutlet}}
                <div id="search-area">
                    <input type="search" placeholder="Search for a fridge" name="search" id="search" maxlength="20" size="20"/>
                </div>
            </header>
            <div id="main-content">
                <aside id ="leftPanel">
                    <div id ="newPost" class="newPost" title="Fill the post and drop it on the fridge">
                        <form id="postForm" accept-charset="utf-8">
                            <div class="content">
                                <textarea name="content" id="content" rows="9" cols="23" maxlength="200" title="Content goes there"></textarea>
                            </div>
                            <div class="author">
                                <input type="text" placeholder="Anonymous" name="author" id="author" maxlength="15" size="12" title="Who are u?"/>
                            </div>
                            <div class="iconBox">
                                <input type="date" id="dueDate" title="Pick a due date"/>
                                <input type="color" id="postColor" class="changeColor" maxlength="15" size="15" value="#f7f083" onchange="javascript:updatePostFormColor(this.value)" title="Change the color"/>
                            </div>
                        </form>
                    </div>
                    <div id="chatPanel">
                        <form id="chatInput" accept-charset="utf-8">
                            <input type="text" value="Anonymous" id="pseudo" min="3" maxlength="15" size="12" title="Choose your pseudo"/>
                            {{outlet chatOutlet}}
                            <textarea id="message" rows="0" cols="0" maxlength="200"></textarea>
                        </form>
                    </div>
                </aside>
                <div id="loaded-content">
                    <div id="loading"></div>
                    {{outlet fridgeOutlet}}
                </div>
            </div>
        </div>
    </script>

	<script  type="text/x-handlebars" data-template-name="header-template">
		<i>Interact with</i> {{view.fridgeId}}
        <a href="/resources/fridge/{{view.fridgeId}}/rss" target="_blank">
            <img src="/images/feed-icon-14x14.png" alt="RSS" style="border:none" />
        </a>
    </script>

    <script  type="text/x-handlebars" data-template-name="message-template">
		<br/><i>Sent at {{view.content.timestamp}}</i>
		<br/><b>{{view.content.user}}:</b> <span class='chatMessage'>{{view.content.message}}</span>
    </script>
        
    <script type="text/x-handlebars" data-template-name="post-template">
		<div class='header'><span class='ui-icon ui-icon-trash'></span></div> 
		<div class='content'>{{{view.generatedContent}}}</div>
		<div class='footer'>
			<span class="author">{{view.content.author}}</span> posted 
			<time class="date" datetime="{{view.content.creationDate}}">{{view.relativeDate}}</time>
		</div>
    </script> 
</body>
	<!-- JS -->
	<noscript>This site requires JavaScript to function properly!</noscript>
</html>