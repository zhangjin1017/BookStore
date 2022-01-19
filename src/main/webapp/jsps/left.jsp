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
    <base target="body">
    <title>图书分类列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
* {
	font-size:10pt;
	text-align:center;
}

div {
	background:#87CEFA;
	margin:3px;
	padding:3px;
}

a {
	text-decoration:none;
}
</style>
  </head>
  
  <body>
<div>
	<a href="<c:url value='/BookServlet?method=findAll' />">全部分类</a>
</div>
<c:forEach var="category" items="${requestScope.categoryList }">
	<div>
		<a href="<c:url value='/BookServlet?method=findByCategory&cid=${pageScope.category.cid }' />">${pageScope.category.cname }</a>
	</div>
</c:forEach>

</html>
