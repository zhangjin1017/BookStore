<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>图书列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
body {
	font-size:10pt;
}

.icon {
	margin:10px;
	border:2px solid gray;
	width:220px;
	height:170px;
	text-align:center;
	float:left;
}
img {
	margin:10px;
}
a:link {
	text-decoration:none;
}
a:hover {
	color:red;
}
</style>
  </head>
  
  <body>
  <c:forEach var="book" items="${requestScope.bookList }">
	<div class="icon">
		<a href="<c:url value='/admin/AdminBookServlet?method=load&bid=${pageScope.book.bid }' />">
			<img src="<c:url value='/${pageScope.book.image }' />" />
		</a>
		<br/>
		<a href="<c:url value='/admin/AdminBookServlet?method=load&bid=${pageScope.book.bid }' />">${pageScope.book.bname }</a>
	</div>
  </c:forEach>
  </body>
</html>
