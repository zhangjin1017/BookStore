<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
	border: 2px solid gray;
	width: 200px;
	height: 125px;
	text-align: center;
}

li {
	margin: 10px;
}

#buy {
	background: url(< c : url value = '/images/YiJianGouMai.jpg'/ >)
		no-repeat;
	display: inline-block;
	background-position: 0 0px;
	width: 116px;
	height: 28px;
}

#buy {
	background: url(< c : url value = '/images/YiJianGouMai.jpg'/ >)
		no-repeat;
	display: inline-block;
	background-position: 0 -28px;
	width: 116px;
	height: 28px;
}
</style>
</head>

<body>
<h1>订单管理</h1>
	<c:forEach var="order" items="${requestScope.orderList }">
		<table border="1" width="100%" cellspacing="0" background="black">
			<tr bgcolor="gray" bordercolor="gray">
				<td colspan="6">
					订单编号：${pageScope.order.oid }<br/>
					成交时间：<fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${pageScope.order.ordertime }" /><br/>
					金额：<font color="red"><b><fmt:formatNumber pattern="0.00" value="${pageScope.order.total }" /></b></font>元<br />
					订单状态：
					<c:choose>
						<c:when test="${pageScope.order.state eq 1 }">
							等待付款
						</c:when>
						<c:when test="${pageScope.order.state eq 2 }">
							<a onclick="return window.confirm('请问你确认要发货吗？');" href="<c:url value='/admin/AdminOrderServlet?method=send&oid=${pageScope.order.oid }' />">等待发货</a>
						</c:when>
						<c:when test="${pageScope.order.state eq 3 }">
							等待确认收货
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
					<td><fmt:formatNumber pattern="0.00" value="${pageScope.orderItem.book.price }" />元</td>
					<td>${pageScope.orderItem.book.author }</td>
					<td>${pageScope.orderItem.count }</td>
					<td><fmt:formatNumber pattern="0.00" value="${pageScope.orderItem.subtotal }" />元</td>
				</tr>
			</c:forEach>
		</table>
	</c:forEach>
</body>
</html>
