<script id="postTemplate" type="text/template">
	<div id={{id}} class='post' style='background-color:{{color}};color:{{textColor}}'>
		<div class='content'>{{content}}</div>
		<div class='author'>{{author}}</div>
		<div class='date'><i>posted {{#relativeDate}} {{date}} {{/relativeDate}}
		{{#dueDate}} and due {{#relativeDate}} {{dueDate}} {{/relativeDate}} {{/dueDate}}</i></div>
	</div>
</script>
	