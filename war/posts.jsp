<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.agourlay.pomf.model.Post"%>
<%@ page import="com.agourlay.pomf.dao.Dao"%>


<%@page import="java.util.ArrayList"%>

<html>
	<head>				
		<meta charset="utf-8">
	</head>
	<body>

		<div id="generated_post">
			<%
				Dao dao = Dao.INSTANCE;
				List<Post> posts = new ArrayList<Post>();
				posts = dao.getPosts();
			%>
		
			<div class="main">
				<div class="fridge">
					<%
						for (Post post : posts) {
					%>
		
					<div id=<%=post.getId()%> class="post draggable">
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
					<%}
					%>
				</div>
			</div>
		</div>
	</body>
</html>		