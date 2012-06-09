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
		<link rel="stylesheet" type="text/css" href="/css/pomf/all.min.css"/>
		<link rel="stylesheet" type="text/css" href="/css/lib/jquery-ui/jquery-ui-1.8.21.custom.css"/>
		<link rel="stylesheet" type="text/css" href="/css/lib/humane/jackedup.css"/>
		<link rel="stylesheet" type="text/css" href="/css/lib/humane/libnotify.css"/>
		<link rel="stylesheet" type="text/css" href="/css/lib/farbastic/farbtastic.css"/>
		<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,600,700,800,300' rel='stylesheet' type='text/css'>	
	
		<script type="text/javascript" src = "/_ah/channel/jsapi"></script>
	</head>
	<body>
		<div id="loading">
			<div id="loading-sign"></div>
			Loading fridge content...
		</div>	
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
								<textarea name=content id="content" rows="8" cols="20" maxlength="140" title="Content goes there"></textarea>
							</div>
							<div class="author">
								by 
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
					<div class ="trash_bin"></div>
				</div>
				<div class="fridge"></div>		
			</div>
			<footer></footer>					
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
	<script type="text/javascript" src = "/js/lib/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src = "/js/lib/jquery-ui-1.8.21.custom.min.js"></script>
	<script type="text/javascript" src = "/js/lib/humane-3.0.0.min.js"></script>
   	<script type="text/javascript" src = "/js/lib/underscore.min.js"></script>
	<script type="text/javascript" src = "/js/lib/farbtastic.min.js"></script>
	<script type="text/javascript" src = "/js/lib/humane-date.min.js"></script>
	<script type="text/javascript" src = "/js/lib/mustache-0.4.2.min.js"></script>
	
	<script type="text/javascript" src = "/js/pomf/all.min.js"></script>
	<noscript>This site requires JavaScript to function properly!</noscript>
</html>