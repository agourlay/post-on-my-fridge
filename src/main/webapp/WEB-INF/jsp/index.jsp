<!DOCTYPE html>
<html lang="en">
<head>
	<!-- META -->
	<title>Post on my fridge : interactive fridge based messaging</title>
	<meta name="description" content="Post on my fridge is an interactive fridge based messaging system">
	<meta name="keywords" content="post on my fridge,social network,messaging,multimedia,fridge,post,interactive,open source" />
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta name="google-site-verification" content="FaoFVgwYaNLDiKf5175qkJiL27JyrLNVZew0Cfmroj8" />
	<meta name="detectify-verification" content="c50776561a50d7fbd7623f72e51be35b" />

	<!-- CSS -->
	<link rel="stylesheet" href="/css/fridge.min.css"/>
	<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.1/themes/ui-lightness/jquery-ui.min.css"/>
	<link rel='stylesheet' href='//fonts.googleapis.com/css?family=Open+Sans:400italic,400,600'/>
	<link rel="stylesheet" href="//bootswatch.com/cosmo/bootstrap.min.css" id="bootstrap-css">
	
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
</head>
<body>
	<noscript>This site requires JavaScript to function properly!</noscript>
	<script src = "//s7.addthis.com/js/300/addthis_widget.js#pubid=ra-50fefe9a3e8b6794"></script>
	<script src = "/_ah/channel/jsapi"></script>
	<script src = "//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script src = "//ajax.googleapis.com/ajax/libs/jqueryui/1.10.1/jquery-ui.min.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/handlebars.js/1.0.0-rc.3/handlebars.min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/ember.js/1.0.0-rc.1/ember.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/moment.js/2.0.0/moment.min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.4.4/underscore-min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/store.js/1.3.6/store.min.js"></script>
 	<script src = "//cdnjs.cloudflare.com/ajax/libs/js-url/1.7.5/js-url.min.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jquery-noty/2.0.3/jquery.noty.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jquery-noty/2.0.3/layouts/top.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jquery-noty/2.0.3/layouts/bottomRight.js"></script>
	<script src = "//cdnjs.cloudflare.com/ajax/libs/jquery-noty/2.0.3/themes/default.js"></script>
	<script src = "/js/fridge.min.js"></script>
	
	<script type="text/x-handlebars" data-template-name="application">
		{{outlet}}
	</script>
	
	<script type="text/x-handlebars" data-template-name="index">
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span10 offset1">
					<p>		
						<div class="addthis_toolbox addthis_default_style ">
							<a class="addthis_button_facebook_like" fb:like:layout="button_count"></a>
							<a class="addthis_button_tweet"></a>
							<a class="addthis_button_pinterest_pinit"></a>
							<a class="addthis_counter addthis_pill_style"></a>
						</div>
					</p>
				</div>
			</div>	
			<div class="row-fluid">		
				<div class="span10 offset1 hero-unit">
					<div class="row-fluid">
						<div class="span12 title-fridge">
							<h1>Post on my fridge</h1>
							<p>
								A collaborative messaging system using sticky notes on virtual fridges.
							</p>
							<div class="span4 offset4">
								<p>
								   {{#linkTo fridge view.demo class="btn btn-primary btn-large" }}
								    	<i class="icon-white icon-eye-open"></i>
										Try the demo now
								   {{/linkTo}}	
								</p>
							</div>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12">
							<a href="#" class="thumbnail">
								<img src="../../images/index-fridge.png" alt="">
							</a>
						</div>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span10 offset1">
					<div class="span6 well">
						<h2>Freedom <i class="icon-user"></i></h2>
						<p>
							Tired of authentication and access management? There is nothing like this here, everyone can add or delete content. Enjoy freedom and chaos.
						</p>
					</div>
					<div class="span6 well">
						<h2>Easy <i class="icon-heart"></i></h2>
						<p>
							To access or create a fridge use the search box or type directly the fridge name in the url /fridge/ <em>fridge name</em>.
						</p>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span10 offset1">
					<div class="span6 well">
						<h2>Share <i class="icon-thumbs-up"></i></h2>
						<p>
							You can share content on a fridge in real time with your friends while chatting.
						</p>
					</div>
					<div class="span6 well">
						<h2>Multimedia <i class="icon-film"></i></h2>
						<p>
							To share a video (youtube/vimeo/dailymotion) or a picture, just insert the url in the content.
						</p>
					</div>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span10 offset1">
					<div class="alert">
					 	<h4>Warning<i class="icon-exclamation-sign"></i></h4>
					 	The application is still under heavy development, the data are likely to be lost, have fun!
					</div>
				</div>
			</div>
		</div>
	</script>
	
    <script type="text/x-handlebars" data-template-name="fridges">
		List of all fridges
    </script>

	<script type="text/x-handlebars" data-template-name="fridge">
        <header>
            <div id="fridge-title">
				<b>Post on fridge {{view.content.name}}</b>
		        <a {{bindAttr href="view.rssUrl"}} target="_blank">
		            <img src="/images/feed-icon-14x14.png" alt="RSS" style="border:none" />
		        </a>
            </div>
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
                        {{render chat}}
                        <textarea id="message" rows="0" cols="0" maxlength="200"></textarea>
                    </form>
                </div>
            </aside>
			{{render "posts" view.content.posts}}
    </script>

    <script type="text/x-handlebars" data-template-name="message-template">
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