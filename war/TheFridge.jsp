<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.agourlay.pomf.model.Post" %>
<%@ page import="com.agourlay.pomf.dao.Dao" %>


<%@page import="java.util.ArrayList"%>

<html>
	<head>
		<title>Posts on the fridge</title>
		<link rel="stylesheet" type="text/css" href="css/main.css"/>
		<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.8.16.custom.css"/>
		<script type="text/javascript" src = "/scripts/jquery-1.6.2.min.js"></script>
		<script type="text/javascript" src = "/scripts/jquery-ui-1.8.16.custom.min.js"></script>
		<meta charset="utf-8"> 
	</head>
	<body>
	<%
	Dao dao = Dao.INSTANCE;
	List<Post> posts = new ArrayList<Post>();
	posts = dao.getPosts();
	%>
	<div class="global">
		<div>
				<div class="line"></div>
				<div class="topLine">
					<div class="headline"><%= posts.size() %> posts on the fridge</div>
				</div>
			</div>
		<div class="info">
			Ingredients in the fridge :<br/>
			<a href="https://github.com/shagaan/PostOnMyFridge" target="blank">https://github.com/shagaan/PostOnMyFridge</a>
			<div class="work-in-progress"></div>
		</div>
		<div class ="input_zone">
			<div class="headline">New post</div>
			
			<form action="/new" method="post" accept-charset="utf-8">
				<table class="input">
					<tr>
						<td>Content</td>
						<td><textarea name=content id="content" rows="3" cols="30">*Type your message*</textarea></td>
					</tr>
					<tr>
						<td>Author</td>
						<td><input type="text" name=author id="author" size="25"/></td>
					</tr>
					<tr>
						<td colspan="2" align="right"><input type="submit" value="Create"/></td>
					</tr>
				</table>
			</form>
		</div>
		<div class="main">	
			<div class="fridge">			
				<% for (Post post : posts) {%>
				
				<div id =<%=post.getId()%> class="post draggable">
					<a class ="delete" href="/remove?id=<%=post.getId()%>">X</a>	
					<div class="content">
						<%=post.getContent()%>
					</div>
					<div class="author">
						by <%=post.getAuthor()%>
					</div>
					<div class="date">
						<i><%=post.getFormatedDate()%></i>
					</div>	
				</div>
				<script>
				$( "#<%=post.getId()%>" ).css('left',<%=post.getPositionX()%>)
				$( "#<%=post.getId()%>" ).css('top',<%=post.getPositionY()%>)
				</script>
				
				<%}
				%>
			</div>
		</div>
	</div>
	</body>
</html>
<script>
	$(function() {
		$( ".draggable" ).draggable({ containment: ".fridge", scroll: true });
		$( ".post" ).hide().fadeIn(1000);
		
		
		
		$( ".post" ).draggable({
			stop: function() {			
				$.ajax({ url: "/update?id="+$(this).attr('id')+"&positionX="+parseInt($(this).css('left'))+"&positionY="+parseInt($(this).css('top'))});
			}
		});
	        
	});
	
	function deletePost(){
		
	}
	
</script>