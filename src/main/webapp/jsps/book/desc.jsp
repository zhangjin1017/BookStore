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
    
    <title>加载图书</title>
    
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
	.divv {
		margin:20px;
		border:2px solid gray;
		width:200px;
		height:200px;
		text-align:center;
	}
	img {
		margin:10px;
		width: 180px;
		height: 180px;
	}
	li {
		margin:10px;
	}
	input {
		margin:0px 0px 0px 50px;
	}
	a {
		background:url(<c:url value='/images/Join.jpg' />) no-repeat;
		display: inline-block;
		
		background-position:0 0px;
		width:126px;
		height:29px;
		margin-left:50px;
	}
	a:HOVER {
		background:url(<c:url value='/images/Join.jpg' />) no-repeat;
		display:inline-block;
		
		background-position:0 -29px;
		width:126px;
		height:29px;
		margin-left:50px;
	}
</style>
  </head>
  
  <body>
<div class="divv">
	<img src="<c:url value='/${requestScope.book.image }' />" border="0" />
</div>
<ul>
	<li>书名：${requestScope.book.bname }</li>
	<li>作者：${requestScope.book.author }</li>
	<li>单价：${requestScope.book.price }元</li>
</ul>
<form id="form" action="<c:url value='/CartServlet' />" method="post">
	<%-- 指定要调用的方法 --%>
	<input type="hidden" name="method" value="add" />
	<%-- CartServlet中的add方法需要bid和count参数 --%>
	<input type="hidden" name="bid" value="${requestScope.book.bid }" />
	<input type="text" size="3" name="count" value="1" />
</form>
<a href="javascript:document.getElementById('form').submit();"></a>
  </body>
</html>
