<!DOCTYPE html>
<html lang="en">
	<head>
	  <!-- META -->
		<title>${fridgeId}'s fridge</title>
		<meta name="description" content="This is ${fridgeId}'s interactive fridge">
        <meta name="keywords" content="post on my fridge,messaging,multimedia,fridge,post,dynamique,social,network,friend,interactive,ajax,javascript,open source" />
       	<meta http-equiv="content-type" content="text/html; charset=utf-8"> 
       	<link rel="alternate" type="application/rss+xml" title="RSS" href="http://post-on-my-fridge.appspot.com/resources/fridge/${fridgeId}/rss" />
	 
       <!-- CSS -->		
		<link rel="stylesheet" type="text/css" href="/css/pomf/fridge.min.css"/>
		<link rel="stylesheet" type="text/css" href="/css/lib/jquery-ui/jquery-ui-1.8.21.custom.css"/>
		<link rel="stylesheet" type="text/css" href="/css/lib/humane/jackedup.css"/>
		<link rel="stylesheet" type="text/css" href="/css/lib/humane/libnotify.css"/>
		<link rel="stylesheet" type="text/css" href="/css/lib/farbastic/farbtastic.css"/>
		<link rel="stylesheet" type="text/css" href="/css/lib/tipsy/tipsy.css"/>
		<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,600,700,800,300' rel='stylesheet' type='text/css'>	
	
		<script type="text/javascript" src = "/_ah/channel/jsapi"></script>
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
					<input type="text" onFocus="JavaScript: clearText(this)"  value="Search for a fridge" name="search" id="search" maxlength="20" size="20"/>
					<button type="button" onclick="JavaScript: redirectAfterSearch()">Go!</button> 
				</div>
			</header>
			<div id="main-content">	
				<div class ="leftPanel">
					<form id="postForm" accept-charset="utf-8">
						<div id ="newPost" class="newPost">
							<div class="content">
								<textarea name="content" id="content" rows="8" cols="20" maxlength="140" title="Content goes there"></textarea>
							</div>
							<div class="author">
								<input type="text" value="Anonymous" name=author id="author" maxlength="15" size="12" title="Who are u?"/>
								<input type="hidden" id="postColor" maxlength="15" size="15" value="#f7f083"/>
								<input type="hidden" id="fridgeId" value="${fridgeId}"/>
							</div>
							<div class="iconBox">
								<input type="hidden" id="dueDate"/>
								<div id="changeColor" class="changeColor" title="Click here to change the post's color">
									<div id="color-dialog" title="Choose the color of your post">
											<div id="color-picker"></div>
									</div>
								</div>	
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
				</div>
				<div id="loaded-content">
					<div id="loading"></div>	
					<div class="fridge"></div>
				</div>
			</div>			
		</div>
	</body>
	<%@ include file="/template/template.jsp" %>
	
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
	<script type="text/javascript" src = "/js/pomf/lib.min.js"></script>
	<script type="text/javascript" src = "/js/pomf/fridge.min.js"></script>
	<noscript>This site requires JavaScript to function properly!</noscript>
</html>