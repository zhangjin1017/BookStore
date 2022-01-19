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
    
    <title>My JSP 'list.jsp' starting page</title>
    
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
	background:url(<c:url value='/images/adminbg.jpg' />);
	text-align:center;
}
table {
	text-align:center;
	border:1px solid purple;
	width:600px;
	border-collapse: collapse;
}

table th {
	background-image:url(<c:url value='/images/admintopbg.jpg' />);
	border:1px solid purple;
	height:50px;
	color:white;
}
table td {
	border:1px solid purple;
	width:300px;
	height:30px;
	background-color:teal;
}

a:LINK {
	color:purple;
	text-decoration:none;
}
a:HOVER {
	color:silver;
}

</style>
  </head>
  
  <body>
<h1>分类列表</h1>
<table align="center">
	<tr>
		<th>分类名称</th>
		<th>操作</th>
	</tr>
	<c:forEach var="category" items="${requestScope.categoryList }">
		<tr>
			<td>${pageScope.category.cname }</td>
			<td>
				<a onclick="return window.confirm('是否要删除该分类？')" href="<c:url value='/admin/AdminCategoryServlet?method=delete&cid=${pageScope.category.cid }' />">删除</a>
				&nbsp;|&nbsp;
				<a href="<c:url value='/admin/AdminCategoryServlet?method=editPre&cid=${pageScope.category.cid }' />">编辑</a>
			</td>
		</tr>
	</c:forEach>
</table>
  </body>

</html>
