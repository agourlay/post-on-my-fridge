<!DOCTYPE html>
<html lang="en">
<head>
	<!-- META -->
	<title>${fridgeId}'s fridge</title>
	<meta name="description" content="This is ${fridgeId}'s interactive fridge">
	<meta name="keywords" content="post on my fridge,social network,messaging,multimedia,fridge,post,interactive,open source" />
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="alternate" type="application/rss+xml" title="RSS" href="http://post-on-my-fridge.appspot.com/resources/fridge/${fridgeId}/rss" />

	<!-- CSS -->
	<link rel="stylesheet" href="/css/app/fridge.min.css"/>
	<link rel="stylesheet" href="/css/lib/jquery-ui/jquery-ui-1.8.23.custom.css"/>
	<link rel="stylesheet" href="/css/lib/humane/jackedup.css"/>
	<link rel="stylesheet" href="/css/lib/humane/libnotify.css"/>
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,600,700,800,300' rel='stylesheet' type='text/css'>

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
	<div id="global">
		<header>
			<div id="fridge-title">
				Interact with "${fridgeId}"
				<a href="http://post-on-my-fridge.appspot.com/resources/fridge/${fridgeId}/rss" target="_blank">
					<img src="/images/feed-icon-14x14.png" alt="RSS" style="border:none" />
				</a>
			</div>
			<div id="search-area">
				<input type="search" placeholder="Search for a fridge" name="search" id="search" maxlength="20" size="20"/>
				<button type="button" onclick="JavaScript: redirectAfterSearch()">Go!</button>
			</div>
		</header>
		<div id="main-content">
			<aside id ="leftPanel">
				<form id="postForm" accept-charset="utf-8">
					<div id ="newPost" class="newPost">
						<div class="content">
							<textarea name="content" id="content" rows="8" cols="20" maxlength="140" title="Content goes there"></textarea>
						</div>
						<div class="author">
							<input type="text" placeholder="Anonymous" name="author" id="author" maxlength="15" size="12" title="Who are u?"/>
							<input type="hidden" id="postColor" maxlength="15" size="15" value="#f7f083"/>
							<input type="hidden" id="fridgeId" value="${fridgeId}"/>
						</div>
						<div class="iconBox">
							<input type="date" id="dueDate" title="Pick a due date"/>
							<input type="color" id="postColor" class="changeColor" maxlength="15" size="15" value="#f7f083" onchange="javascript:updatePostFormColor(this.value)" title="Change the color"/>
						</div>
					</div>
				</form>
				<div id="chatPanel">
					<form id="chatInput" accept-charset="utf-8">
						<input type="text" value="Anonymous" id="pseudo" min="3" maxlength="15" size="12" title="Choose your pseudo"/>
						<div id="chatLog"></div>
						<textarea id="message" rows="0" cols="0" maxlength="200" title="Type your message here"></textarea>
					</form>
				</div>
			</aside>
			<div id="loaded-content">
				<div id="loading"></div>
				<section id="fridge">
					<script type="text/x-handlebars">
							    {{#each App.FridgeController}}
									{{#view App.PostView postBinding="this"}}
											<div class='header'><span class='ui-icon ui-icon-trash'></span></div> 
											<div class='content'>{{{view.generatedContent}}}</div>
											<div class='footer'>
												<span class="author">{{author}}</span> posted 
												<time class="date" datetime="{{date}}">{{view.relativeDate}}</time>
											</div>
									 {{/view}}
								{{/each}}
    					  </script>
				</section>
			</div>
		</div>
	</div>
</body>
	<%@ include file="/js/app/templates/chatMessage.jsp" %>
	<!-- JS -->
	<script src = "/js/app/lib.min.js"></script>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
	<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script>
	<script src = "/js/app/lib.jquery.min.js"></script>
	<script src = "/js/app/fridge.js"></script>
	<noscript>This site requires JavaScript to function properly!</noscript>
</html>