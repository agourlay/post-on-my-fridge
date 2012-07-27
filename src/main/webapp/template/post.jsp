<script id="postTemplate" type="text/x-handlebars-template">
	<article id={{id}} class='post' style='background-color:{{color}};color:{{textColor}}'>
		<div class='header'><span class='ui-icon ui-icon-trash'></span></div> 
		<div class='content'>{{{content}}}</div>
		<div class='footer'>
			<span class="author">{{author}}</span> posted <time class="date" datetime="{{date}}">{{relativeDate}}</time>
		</div>
	</article>
</script>
	