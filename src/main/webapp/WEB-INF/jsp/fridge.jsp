<!DOCTYPE html>
<html lang="en">
<head>
	<!-- META -->
	<meta name="description" content="This is an interactive fridge">
	<meta name="keywords" content="post on my fridge, social network, messaging, multimedia, fridge, post, interactive" />
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>a super fridge</title>

	<!-- CSS -->
	<link rel="stylesheet" href="/css/fridge.min.css"/>
	<link rel="stylesheet" href="//ajax.aspnetcdn.com/ajax/jquery.ui/1.10.0/themes/ui-lightness/jquery-ui.css"/>
	<link rel='stylesheet' href='//fonts.googleapis.com/css?family=Open+Sans:400italic,400,600'/>
	
	<!-- JS -->
	<script type="text/javascript">
	  	var _gaq = _gaq || [];
	  	_gaq.push(['_setAccount', 'UA-25345034-1']);
	  	_gaq.push(['_setSiteSpeedSampleRate', 100]);
	  	_gaq.push(['_trackPageview']);
	   
	  	(function() {
	    	var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    	ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    	var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  	})();
	</script>
	<script type="text/javascript" src="//s7.addthis.com/js/300/addthis_widget.js#pubid=ra-50fefe9a3e8b6794"></script>
</head>
<body>
	<noscript>This site requires JavaScript to function properly!</noscript>
	<script src = "/_ah/channel/jsapi"></script>
    <script src = "//cdnjs.cloudflare.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jqueryui/1.10.0/jquery-ui.min.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/handlebars.js/1.0.rc.2/handlebars.min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/ember.js/1.0.0-pre.4/ember.min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/moment.js/1.7.2/moment.min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.4.3/underscore-min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/store.js/1.3.6/store.min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/js-url/1.7.5/js-url.min.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jquery-noty/2.0.3/jquery.noty.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jquery-noty/2.0.3/layouts/top.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jquery-noty/2.0.3/layouts/bottomRight.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jquery-noty/2.0.3/themes/default.js"></script>
	<script src = "/js/fridge.min.js"></script>

	<div class="addthis_toolbox addthis_floating_style addthis_32x32_style" style="right:1px;top:150px;">
		<a class="addthis_button_preferred_1"></a>
		<a class="addthis_button_preferred_2"></a>
		<a class="addthis_button_preferred_3"></a>
		<a class="addthis_button_preferred_4"></a>
		<a class="addthis_button_compact"></a>
	</div>
		
	<script type="text/x-handlebars" data-template-name="application">
        <div id="global">
            <header>
                {{outlet header}}
                <div id="search-area">
                    <input type="search" placeholder="Search for a fridge" name="search" id="search" maxlength="20" size="20" x-webkit-speech/>
                </div>
            </header>
            <div id="main-content">
                <aside id ="leftPanel">
                    <div id ="newPost" class="newPost">
                        <form id="postForm" accept-charset="utf-8">
                            <div class="content">
                                <textarea name="content" id="content" rows="9" cols="23" maxlength="200" placeholder="Drop me on the fridge"></textarea>
                            </div>
                            <div class="author">
                                <input type="text" placeholder="Anonymous" name="author" id="author" maxlength="15" size="12"/>
                            </div>
                            <div class="iconBox">
                                <input type="date" id="dueDate" title="Due date"/>
                                <input type="color" id="postColor" class="changeColor" value="#f7f083" onchange="javascript:updatePostFormColor(this.value)"/>
                            </div>
                        </form>
                    </div>
                    <div id="chatPanel">
                        <form id="chatInput" accept-charset="utf-8">
                            <input type="text" placeholder="Anonymous" id="pseudo" min="3" maxlength="15" size="12" title="Choose your pseudo"/>
                            {{outlet chat}}
                            <textarea id="message" rows="0" cols="0" maxlength="200"></textarea>
                        </form>
                    </div>
                </aside>
				{{outlet fridge}}
            </div>
        </div>
    </script>

	<script  type="text/x-handlebars" data-template-name="header-template">
		<b>Post on fridge {{view.fridgeId}}</b>
        <a {{bindAttr href="view.rssUrl"}} target="_blank">
            <img src="/images/feed-icon-14x14.png" alt="RSS" style="border:none" />
        </a>
    </script>

    <script  type="text/x-handlebars" data-template-name="message-template">
		<br/><i>Sent at {{view.content.date}}</i>
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
</html>