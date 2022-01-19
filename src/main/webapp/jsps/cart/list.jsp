<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>购物车列表</title>
    
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
		font-size:11pt;
	}
	.imgg {
		border:2px solid gray;
	}
	a {
		text-decoration: none;
	}
	a:HOVER {
		color:red;
	}
	#buy {
		background: url(<c:url value='/images/YiJianGouMai.jpg' />) no-repeat;
		display: inline-block;
		
		background-position:0 0px;
		width:116px;
		height:28px;
	}
	#buy:HOVER {
		background: url(<c:url value='/images/YiJianGouMai.jpg' />) no-repeat;
		display: inline-block;
		
		background-position:0 -28px;
		width:116px;
		height:28px;
	}
</style>
  </head>
  
  <body>
<h1 align="center" style="font-family: 华文行楷; font-size:38px;">购物车</h1>
		<c:choose>
			<%-- 如果没有购物车，或者购物车的内容集合长度为0 --%>
			<c:when test="${empty sessionScope.cart or fn:length(sessionScope.cart.cartItems) eq 0 }">
				<h1>你没有添加任何商品！！！>_< </h1>
			</c:when>
			<c:otherwise>
				<table border="1" width="100%" cellspacing="0" background="black">
					<tr>
						<td colspan="7" align="right" style="font-size:15pt; font-weight:900">
							<a href="<c:url value='/CartServlet?method=clear' />">清空购物车</a>
						</td>
					</tr>
					<tr>
						<th>图片</th>
						<th>书名</th>
						<th>作者</th>
						<th>单价</th>
						<th>数量</th>
						<th>小计</th>
						<th>操作</th>
					</tr>
					
					<c:forEach var="cartItem" items="${sessionScope.cart.cartItems }">
						<tr align="center">
							<td width="129px;">
								<img class="imgg" src="<c:url value='/${pageScope.cartItem.book.image }' />" />
							</td>
							<td>${pageScope.cartItem.book.bname }</td>
							<td>${pageScope.cartItem.book.author }</td>
							<td>${pageScope.cartItem.book.price }元</td>
							<td>${pageScope.cartItem.count }</td>
							<td>${pageScope.cartItem.subtotal }元</td>
							<td><a href="<c:url value='/CartServlet?method=delete&bid=${pageScope.cartItem.book.bid }' />">删除</a></td>
						</tr>
					</c:forEach>
					
					<tr>
						<td colspan="7" align="right" style="font-size:15pt; font-weight:900">
							合计：${sessionScope.cart.total }元
						</td>
					</tr>
					<tr>
						<td colspan="7" align="right" style="font-size:15pt; font-weight:900">
							<a id="buy" href="<c:url value='/OrderServlet?method=add' />"></a>
						</td>
					</tr>
				</table>
			</c:otherwise>
		</c:choose>
  </body>
</html>
