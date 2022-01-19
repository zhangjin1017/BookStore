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
    
    <title>My JSP 'login.jsp' starting page</title>
    
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
	text-align:center;
}
form {
	margin:60px 0px 0px 0px;
}
input {
	margin:10px;
}
</style>
  </head>
  
  <body>
	<h1 align="center">管理员登录页面</h1>
	<hr/>
	<br/>
	<p style="color:red; font-weight:900">${requestScope.msg }</p>
	<form action="<c:url value='/UserServlet' />" method="POST">
		<input type="hidden" name="method" value="login" />
		管理员账户：<input type="text" name="username" value="${requestScope.user.username }" />
		<span style="color:red; font-weight:900">${requestScope.errors.username }</span>
		<br/>
		密
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		码：<input type="password" name="password" value="${requestScope.user.password }" />
		<span style="color:red; font-weight:900">${requestScope.errors.password }</span>
		<br/>
		<input type="submit" value="进入后台" />
	</form>
  </body>
</html>
