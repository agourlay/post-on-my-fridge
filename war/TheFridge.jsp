<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.agourlay.pomf.model.Post" %>
<%@ page import="com.agourlay.pomf.dao.Dao" %>


<%@page import="java.util.ArrayList"%>

<html>
	<head>
		<title>Posts</title>
		<link rel="stylesheet" type="text/css" href="css/main.css"/>
		  <meta charset="utf-8"> 
	</head>
	<body>
<%
Dao dao = Dao.INSTANCE;
List<Post> posts = new ArrayList<Post>();
posts = dao.getPosts();
%>
	<div style="width: 100%;">
		<div class="line"></div>
		<div class="topLine">
			<div style="float: left;" class="headline">Post</div>
		</div>
	</div>

<div style="clear: both;"/>	
You have a total number of <%= posts.size() %>  Posts.

<table>
  <tr>
      <th>Author </th>
      <th>Content</th>
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
<a class="delete" href="/remove?id=<%=post.getId()%>">Delete</a>
</td>
</tr> 
<%}
%>
</table>


<hr />

<div class="main">

<div class="headline">New post</div>

<form action="/new" method="post" accept-charset="utf-8">
	<table>
		<tr>
			<td><label for="summary">Content</label></td>
			<td><input type="text" name="content" id="content" size="65"/></td>
		</tr>
		<tr>
			<td><label for="author">Author</label></td>
			<td><input type="text" name=author id="author" size="65"/></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><input type="submit" value="Create"/></td>
		</tr>
	</table>
</form>
</div>
</body>
</html>
