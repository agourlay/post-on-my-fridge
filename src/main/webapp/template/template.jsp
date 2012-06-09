<script id="postTemplate" type="text/template">
	<div id={{id}} class='post' style='background-color:{{color}};color:{{textColor}}'>
		<div class='content'>{{content}}</div>
		<div class='footer'>
			{{author}} posted {{#relativeDate}} {{date}} {{/relativeDate}}
		</div>
	</div>
</script>
	