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
    
    <title>Top</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
h1 {
	font-family:"华文行楷";
	font-size:50px;
	color:navy;
	margin:30px;
	text-align:center;
}
a {
	text-decoration:none;
}
a:hover {
	color:navy;
}
</style>
  </head>
  
  <body>
<h1>游子书屋</h1>
<c:choose>
	<c:when test="${empty sessionScope.user}">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="<c:url value='/jsps/user/login.jsp' />" target="_parent">登录</a>&nbsp;&nbsp;|&nbsp;
		<a href="<c:url value='/jsps/user/regist.jsp' />" target="_parent">注册</a>
	</c:when>
	<c:otherwise>
		&nbsp;&nbsp;&nbsp;&nbsp;
		您好：${sessionScope.user.username }&nbsp;&nbsp;|&nbsp;
		<a href="<c:url value='/jsps/cart/list.jsp' />" target="body">我的购物车</a>&nbsp;&nbsp;|&nbsp;
		<a href="<c:url value='/OrderServlet?method=myOrders' />" target="body">我的订单</a>&nbsp;&nbsp;|&nbsp;
		<a href="<c:url value='/UserServlet?method=quit' />" target="_parent">退出</a>
	</c:otherwise>
</c:choose>
  </body>
</html>
