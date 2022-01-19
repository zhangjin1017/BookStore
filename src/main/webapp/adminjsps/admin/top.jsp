<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
body {
	color:white;
	font-family:"宋体";
	background-image:url(<c:url value='/images/admintopbg.jpg' />);
}
h1 {
	margin:20px;
}
p {
	margin:30px 0px 0px 30px;
}
</style>
  </head>
  
  <body>
<div class="divv">
<h1 align="center">~游子书屋之Administrator后台管理~</h1>
<p>管理员：${sessionScope.user.username }您好！</p>
</div>
  </body>
</html>
