<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
	<head>
		<title>Posts on the fridge</title>
		
		<link rel="stylesheet" type="text/css" href="css/main.css"/>
		<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css"/>
		<link rel="stylesheet" type="text/css" href="css/captcha.css"/>
		
		<script language="javascript" type="text/javascript" src = "/scripts/jquery-1.6.3.min.js"></script>
		<script language="javascript" type="text/javascript" src = "/scripts/jquery-ui-1.8.16.custom.min.js"></script>
		<script language="javascript" type="text/javascript" src = "/scripts/jquery-captcha.js"></script>
		<script language="javascript" type="text/javascript" src = "/scripts/jYoutube.js"></script>
		<script language="javascript" type="text/javascript" src = "/scripts/post-on-my-fridge.js"></script>
		
		<script language="javascript" type="text/javascript" src = "/scripts/browser-update.js"></script>
		<script language="javascript" type="text/javascript" src = "/scripts/google-analytics.js"></script>
			
		<meta charset="utf-8">
	</head>
	<body>
	
	<div class="global">
		<div class="info">
			Ingredients in the fridge :<br/>
			<a href="https://github.com/shagaan/PostOnMyFridge" target="blank">https://github.com/shagaan/PostOnMyFridge</a>
			<div class="work-in-progress"></div>
			<img src="http://code.google.com/appengine/images/appengine-silver-120x30.gif" alt="Powered by Google App Engine" />
		</div>
		<div class ="input_zone">
			<div class="headline">New post</div>
			
			<form id="postForm" accept-charset="utf-8">
				<table class="input">
					<tr>
						<td>Content</td>
						<td><textarea name=content id="content" rows="3" cols="30"></textarea></td>
					</tr>
					<tr>
						<td>Author</td>
						<td><input type="text" name=author id="author" size="25"/></td>
					</tr>
					<tr>
						<td colspan="2" align="right"><input type="button" value="Create" onclick="creationRequest()"/></td>
					</tr>
				</table>
				<div class="ajax-fc-container"></div>
			</form>
			<div class ="trash_bin"></div>
		</div>
			
		<div id="injected_post">Loading posts...</div>
		
		
	</div>
	</body>
</html>