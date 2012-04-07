<!DOCTYPE html>
<html>
	<head>
		<title>Post on your fridge</title>
		<meta name="description" content="Post on my fridge is an interactive fridge based messaging system">
        <meta name="keywords" content="post on my fridge,messaging,multimedia,fridge,post,dynamique,social,network,friend,interactive,ajax,javascript,open source" />
       	<meta http-equiv="content-type" content="text/html; charset=utf-8">  
     		
		<link rel="stylesheet" type="text/css" href="css/pomf/layout.css"/>
		<link rel="stylesheet" type="text/css" href="css/pomf/post.css"/>
		<link rel="stylesheet" type="text/css" href="css/lib/jquery-ui-1.8.16.custom.css"/>
		<link rel="stylesheet" type="text/css" href="css/lib/jquery.jgrowl.css"/>
		<link rel="stylesheet" type="text/css" href="css/lib/captcha.css"/>
		<link rel="stylesheet" type="text/css" href="css/lib/farbtastic.css"/>
		<link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,600,700,800,300' rel='stylesheet' type='text/css'>

		<link rel="alternate" type="application/rss+xml" title="RSS" href="http://post-on-my-fridge.appspot.com/rss/fridge.rss" />
	</head>
	<body>
		<div id="loading">
			<div id="loading-sign"></div>
			Loading fridge content...
		</div>	
		<div id="global">
			<div id="header"></div>	
			<div class ="leftPanel">
				<form id="postForm" accept-charset="utf-8">
					<div id ="newPost" class="newPost">
						<div class="content">
							<textarea name=content id="content" rows="8" cols="20" maxlength="100" title="Content goes there"></textarea>
						</div>
						<div class="author">
							by 
							<input type="text" value="Anonymous" name=author id="author" maxlength="15" size="12" title="Who are u?"/>
							<input type="hidden" id="postColor" maxlength="15" size="15" value="f7f083"/>
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
					<div class="ajax-fc-container"></div>
				</form>
				<div class ="trash_bin"></div>
			</div>
			<div class="fridge"></div>							
		</div>
	</body>
	
	<script type="text/javascript" src = "/scripts/lib/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src = "/scripts/lib/jquery-ui-1.8.18.custom.min.js"></script>
	<script type="text/javascript" src = "/scripts/lib/jquery.jgrowl.min.js"></script>
	<script type="text/javascript" src = "/scripts/lib/jquery-captcha.js"></script>
        <script type="text/javascript" src = "/scripts/lib/jquery.konami.js"></script>
	<script type="text/javascript" src = "/scripts/lib/farbtastic.js"></script>
	<script type="text/javascript" src = "/scripts/lib/humane.js"></script>			
	<script type="text/javascript" src = "/scripts/lib/browser-update.js"></script>
	<script type="text/javascript" src = "/scripts/lib/google-analytics.js"></script>
	<script type="text/javascript" src = "/scripts/lib/mustache.js"></script>	
	<script type="text/javascript" src = "/scripts/pomf/post.js"></script>	
	<script type="text/javascript" src = "/scripts/pomf/tools.js"></script>	
	<script type="text/javascript" src = "/scripts/pomf/init.js"></script>	
	
	<script id="postTemplate" type="text/html">
	<div id={{id}} class='post' style='background-color:{{color}};color:{{textColor}}'>
		<div class='content'>{{content}}</div>
		<div class='author'>{{author}}</div>
		<div class='date'><i>posted {{#relativeDate}} {{date}} {{/relativeDate}}
		{{#dueDate}} and due {{#relativeDate}} {{dueDate}} {{/relativeDate}} {{/dueDate}}</i></div>
	</div>
	</script>
	
</html>