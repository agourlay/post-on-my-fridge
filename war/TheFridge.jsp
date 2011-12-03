<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
	<head>
		<title>Posts on the fridge</title>
		
		<link rel="stylesheet" type="text/css" href="css/main.css"/>
		<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css"/>
		<link rel="stylesheet" type="text/css" href="css/jquery.jgrowl.css"/>
		<link rel="stylesheet" type="text/css" href="css/captcha.css"/>
		<link rel="stylesheet" type="text/css" href="css/farbtastic.css"/>
		
		<meta charset="utf-8">
	</head>
	<body>	
		<div class="header">
		
		</div>
		
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
							<input type="text" name=author id="author" maxlength="15" size="12" title="Who are u?"/>
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
				
			</div>	
					
			<div class="fridge"></div>
												
		</div>
	</body>
	
	<script type="text/javascript" src = "/scripts/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src = "/scripts/jquery-ui-1.8.16.custom.min.js"></script>
	<script type="text/javascript" src = "/scripts/jquery.jgrowl.min.js"></script>
	<script type="text/javascript" src = "/scripts/jquery-captcha.js"></script>
	<script type="text/javascript" src = "/scripts/farbtastic.js"></script>
	<script type="text/javascript" src = "/scripts/post-on-my-fridge.js"></script>	
	<script type="text/javascript" src = "/scripts/browser-update.js"></script>
	<script type="text/javascript" src = "/scripts/google-analytics.js"></script>
		
</html>