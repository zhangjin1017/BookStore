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
    
    <title>Login</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
p {
	font-size: 24px;
}
</style>
  </head>
  
  <body>
<h1>登录</h1>
<p style="color:red;font-weight:900">${requestScope.msg }</p>
<form action="<c:url value='/UserServlet' />" method="POST" target="_top">
	<input type="hidden" name="method" value="login" />
	用户名：<input type="text" name="username" value="${requestScope.user.username }" />
	<span style="color:red; font-weight:900">${requestScope.errors.username }</span>
	<br/>
	密    码：<input type="password" name="password" value="${requestScope.user.password }" />
	<span style="color:red; font-weight:900">${requestScope.errors.password }</span>
	<br/>
	<input type="submit" value="登录" />
</form>
  </body>
</html>
