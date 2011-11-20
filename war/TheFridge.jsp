<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
	<head>
		<title>Posts on the fridge</title>
		
		<link rel="stylesheet" type="text/css" href="css/main.css"/>
		<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css"/>
		<link rel="stylesheet" type="text/css" href="css/captcha.css"/>
		<link rel="stylesheet" type="text/css" href="css/farbtastic.css"/>
		
		<script language="javascript" type="text/javascript" src = "/scripts/jquery-1.7.min.js"></script>
		<script language="javascript" type="text/javascript" src = "/scripts/jquery-ui-1.8.16.custom.min.js"></script>
		<script language="javascript" type="text/javascript" src = "/scripts/jquery-captcha.js"></script>
		<script language="javascript" type="text/javascript" src = "/scripts/farbtastic.js"></script>
		<script language="javascript" type="text/javascript" src = "/scripts/post-on-my-fridge.js"></script>
		
		<script language="javascript" type="text/javascript" src = "/scripts/browser-update.js"></script>
		<script language="javascript" type="text/javascript" src = "/scripts/google-analytics.js"></script>
			
		<meta charset="utf-8">
	</head>
	<body>
	<a href="http://github.com/shagaan/PostOnMyFridge"><img style="position: absolute; top: 0; right: 0; border: 0;" src="https://a248.e.akamai.net/assets.github.com/img/7afbc8b248c68eb468279e8c17986ad46549fb71/687474703a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f6461726b626c75655f3132313632312e706e67" alt="Fork me on GitHub"></a>
	
	<div class="header"></div>
	
	<div class="global">				
		<div class ="leftPanel">
			<div class="headline">
				<ol>
	  				<li>Fill in the post. </li>
	  				<li>Choose a due date. (optionnal) </li>
	  				<li>Choose a color. (optionnal) </li>
					<li>Solve the captcha.</li>
					<li>Drop the post on the fridge.</li>
				</ol>
			</div>
			<form id="postForm" accept-charset="utf-8">
				<div id ="newPost" class="newPost">
					<div class="content">
						<textarea name=content id="content" rows="8" cols="20" maxlength="100" title="Content goes there"></textarea>
					</div>
					<div class="author">
						<input type="text" name=author id="author" maxlength="15" size="15" title="Who are u?"/>
						<input type="hidden" id="postColor" maxlength="15" size="15" value="f7f083"/>
						<input type="hidden" id="dueDate"/>
					</div>
					<div id="changeColor" class="changeColor" title="Click here to change the post color">
						<div id="color-dialog" title="Choose the color of your post">
								<div id="color-picker"></div>
						</div>
					</div>	
				</div>				
				<div class="ajax-fc-container"></div>
			</form>
			
			<div class ="trash_bin"></div>
			
			<div class="info">
				<img src="http://code.google.com/appengine/images/appengine-silver-120x30.gif" alt="Powered by Google App Engine" />
			</div>
			
		</div>	
				
		<div class="fridge"></div>
											
	</div>
	</body>
</html>