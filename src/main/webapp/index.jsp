<!DOCTYPE html>
<html lang="en">
	<head>
		<!-- META -->
		<title>Post on my fridge : interactive fridge based messaging</title>
		<meta name="description" content="Post on my fridge is an interactive fridge based messaging system">
        	<meta name="keywords" content="post on my fridge,social network,messaging,multimedia,fridge,post,interactive,ajax,open source" />
       		<meta charset="utf-8"> 
		<meta name="google-site-verification" content="FaoFVgwYaNLDiKf5175qkJiL27JyrLNVZew0Cfmroj8" />
	 	
        	<!-- CSS -->		
		<link rel="stylesheet" href="/css/pomf/fridge.min.css"/>
		<link rel="stylesheet" href="/css/lib/jquery-ui/jquery-ui-1.8.22.custom.css"/>
		<link rel="stylesheet" href="/css/lib/humane/jackedup.css"/>
		<link rel="stylesheet" href="/css/lib/humane/libnotify.css"/>
		<link rel="stylesheet" href="/css/lib/farbastic/farbtastic.css"/>
		<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,600,700,800,300' rel='stylesheet' type='text/css'>	
	</head>
	<body>
 		<div id="global">
			<header>
				<div id="fridge-title">
					Welcome to "Post on my fridge"</div>
				<div id="search-area">
					<input type="text" onFocus="JavaScript: clearText(this)"  value="Search for a fridge" name="search" id="search" maxlength="20" size="20"/>
					<button type="button" onclick="JavaScript: redirectAfterSearch()">Go!</button> 
				</div>
			</header>
			<div id ="index-content">
				<div id ="index-description">
				 	Post on my fridge is a collaborative messaging system using sticky notes on virtual fridges.
					<br/>
					<br/>
					There is no authentication, everyone can add or delete content.
					<br/>
					To access or create a fridge use the search box or type directly the fridge name in the url.
					<br/>
					You can share content on a fridge in real time with your friends!
					<br/>
					<br/>
					Posts can contain dynamic media content. Just insert an url in the content of a post:
					<br/>
					<ul>
						<li>a link to a gif/jpeg/png will display the picture in the post.</li>
						<li>a youtube video will embed a video player in the post.</li>
						<li>a link to a twitter account will display the last tweet.</li>
						<li>a link to a rss feed will display the last feed.</li>
					</ul>				
					<br/>
					Fridge urls can be shared, send them to your friend to share your latest web content.
					<br/>	
					<br/>		
					Try the <a href="http://post-on-my-fridge.appspot.com/fridge/demo" target="_blank">demo fridge</a> now!
				</div>
				<div id ="index-picture"></div>				
			</div>
		    <footer>
		    	<a href="mailto:arnaud.gourlay@gmail.com" target="_blank"> Contact </a>
		    		 -
		    	<a href="http://about.arnaud-gourlay.info/" target="_blank"> About the author </a>
		    		 -
		    	<a href="https://github.com/shagaan/Post-On-My-Fridge" target="_blank"> Github </a>	 
		    </footer>						
		</div>
	</body>
	
	<!-- JS -->
	<script src = "/js/pomf/lib.min.js"></script>
	<script src="/js/pomf/tools.js"></script>
	<script>	
	  var _gaq = _gaq || [];
	  _gaq.push(['_setAccount', 'UA-25345034-1']);
	  _gaq.push(['_trackPageview']);
	   
	  (function() {
		$('#global').css({opacity: 0.0, visibility: "visible"}).animate({opacity: 1.0});
		setupSearchAutocomplete();
		setRandomBackGround();
		
	    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
	    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
	    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
	  })();
	</script>
	<noscript>This site requires JavaScript to function properly!</noscript>
</html>

