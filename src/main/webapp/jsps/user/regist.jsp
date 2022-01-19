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
    
    <title>Regist</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
<h1>注册</h1>
<%-- 
1，显示errors  -->  字段错误
2，显示异常错误
3，回显
 --%> 
<p style="color:red; font-weight:900">${requestScope.msg }</p>
<form action="<c:url value='/UserServlet' />" method="POST">
	<!-- 给出method参数，指定调用servlet中的方法名 -->
	<input type="hidden" name="method" value="register" />
	用户名：<input type="text" name="username" value="${requestScope.user.username }" />
	<span style="color:red; font-weight:900">${requestScope.errors.username }</span>
	<br/>
	密    码：<input type="password" name="password" value="${requestScope.user.password }" />
	<span style="color:red; font-weight:900">${requestScope.errors.password }</span>
	<br/>
	邮    箱：<input type="text" name="email" value="${requestScope.user.email }" />
	<span style="color:red; font-weight:900">${requestScope.errors.email }</span>
	<br/>
	<input type="submit" value="注册" />
</form>
  </body>
</html>
