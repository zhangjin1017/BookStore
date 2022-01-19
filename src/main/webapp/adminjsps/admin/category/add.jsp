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
    
    <title>My JSP 'add.jsp' starting page</title>
    
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
}
.divv {
	margin:50px;
}
</style>
  </head>
  
  <body>
  <div class="divv">
	<h1>添加分类</h1>
	<p style="font-weight:900; color:red;">${requestScope.msg }</p>
	<form action="<c:url value='/admin/AdminCategoryServlet' />" method="post">
		<input type="hidden" name="method" value="add" />
		分类名称：<input type="text" name="cname" value="${requestScope.category.cname }" />
		<input type="submit" value="添加分类" />
	</form>
  </div>
  </body>
</html>
