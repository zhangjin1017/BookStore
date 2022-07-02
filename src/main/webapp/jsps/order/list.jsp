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
	* {
		font-size: 11pt;
	}
	.divv {
		border:2px solid gray;
		width:200px;
		height:125px;
		text-align:center;
	}
	img{
		margin:10px;
		width: 180px;
		height: 100px;
	}
	li {
		margin:10px;
	}
	#buy {
		background:url(<c:url value='/images/YiJianGouMai.jpg' />) no-repeat;
		display:inline-block;
		
		background-position:0 0px;
		width:116px;
		height:28px;
	}
	#buy {
		background:url(<c:url value='/images/YiJianGouMai.jpg' />) no-repeat;
		display:inline-block;
		
		background-position:0 -28px;
		width:116px;
		height:28px;
	}
</style>
  </head>
  
  <body>
<h1>我的订单</h1>
<c:choose>
	<c:when test="${empty requestScope.orderList or fn:length(requestScope.orderList) eq 0 }">
		你还没有添加任何订单噢~~~  ~_~
	</c:when>
	<c:otherwise>
		<table border="1" width="100%" cellspacing="0" background="black">
		<c:forEach var="order" items="${requestScope.orderList }">
			<tr bgcolor="gray" bordercolor="gray">
				<td colspan="6">
					订单编号：${pageScope.order.oid }<br/>
					成交时间：<fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${pageScope.order.ordertime }" /><br/>
					金额：<font color="red"><b>${pageScope.order.total }</b></font>元<br/>
					订单状态：
					<c:choose>
						<c:when test="${pageScope.order.state eq 1 }">
							<a href="<c:url value='/OrderServlet?method=load&oid=${pageScope.order.oid }' />">待付款</a>
						</c:when>
						<c:when test="${pageScope.order.state eq 2 }">
							等待发货
						</c:when>
						<c:when test="${pageScope.order.state eq 3 }">
							<a href="<c:url value='/OrderServlet?method=confirm&oid=${pageScope.order.oid }' />" >确认收货</a>
						</c:when>
						<c:when test="${pageScope.order.state eq 4 }">
							交易成功
						</c:when>
					</c:choose>
				</td>
			</tr>
			<tr>
				<th>图书封面</th>
				<th>书名</th>
				<th>单价（单位：元）</th>
				<th>作者</th>
				<th>数量</th>
				<th>小计（单位：元）</th>
			</tr>
			<c:forEach var="orderItem" items="${pageScope.order.orderItemList }">
				<tr bordercolor="gray" align="center">
					<td width="15%">
						<div class="divv">
							<img src="<c:url value='/${pageScope.orderItem.book.image }' />" />
						</div>
					</td>
					<td>${pageScope.orderItem.book.bname }</td>
					<td>${pageScope.orderItem.book.price }元</td>
					<td>${pageScope.orderItem.book.author }</td>
					<td>${pageScope.orderItem.count }</td>
					<td>${pageScope.orderItem.subtotal }元</td>
				</tr>
			</c:forEach>
		</c:forEach>
		</table>
	</c:otherwise>
</c:choose>
  </body>
</html>
