<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.agourlay.pomf.model.Post" %>
<%@ page import="com.agourlay.pomf.dao.Dao" %>


<%@page import="java.util.ArrayList"%>

<html>
	<head>
		<title>Posts on the fridge</title>
		<link rel="stylesheet" type="text/css" href="css/main.css"/>
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
			<table class="fridge">
			  <tr>
			      <th>Author </th>
			      <th>Content</th>
			      <th>Creation date</th>
			      <th>Delete</th>
			  </tr>
			
				<% for (Post post : posts) {%>
				<tr> 
				<td>
				<%=post.getAuthor()%>
				</td>
				<td>
				<%=post.getContent()%>
				</td>
				<td>
				<%=post.getFormatedDate()%>
				</td>
				<td>
				<a class="delete" href="/remove?id=<%=post.getId()%>">Delete</a>
				</td>
				</tr> 
				<%}
				%>
			</table>
		</div>
	</div>
	</body>
</html>
