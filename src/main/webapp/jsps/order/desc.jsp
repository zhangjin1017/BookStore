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
    
    <title>订单详细</title>
    
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
	
	#pay {
		background: url(<c:url value='/images/JieSuan.jpg' />) no-repeat;
		display: inline-block;
	
		background-position:0 0px;
		width:116px;
		height:29px;
	}
	#pay:HOVER {
		background: url(<c:url value='/images/JieSuan.jpg' />) no-repeat;
		display: inline-block;
		
		background-position:0 -29px;
		width:116px;
		height:29px;
	}
</style>
  </head>
  
  <body>
<h1>当前订单</h1>
<table border="1" width="100%" cellspacing="0" background="black">
	<tr bgcolor="gray" bordercolor="gray">
		<td colspan="6">
			订单编号：${requestScope.order.oid }<br/>
			成交时间：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${requestScope.order.ordertime }" /><br/>
			金额：<font color="red"><b>${requestScope.order.total }</b></font>元
		</td>
	</tr>
	<tr>
		<th>图书封面</th>
		<th>书名</th>
		<th>单价</th>
		<th>作者</th>
		<th>数量</th>
		<th>小计</th>
	</tr>
	<c:forEach var="orderItem" items="${requestScope.order.orderItemList }">
		<tr bordercolor="gray" align="center">
			<td width="129px">
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
</table>
<br/>
<form id="form" action="<c:url value='/OrderServlet'/>" method="POST" target="">
	<!-- 只要点击支付就会调用servlet中的pay方法 -->
	<input type="hidden" name="method" value="zhiFu" />
	<!-- 传递oid参数，让servlet知道是哪个订单要支付 -->
	<input type="hidden" name="oid" value="${requestScope.order.oid }" />
	收货地址：<input type="text" name="address" size="50" value="地球" /><br/>
	选择支付通道：
	<input type="radio" name="pd_FrpId" value="1000000-NET" checked="checked" />易宝支付
	<input type="radio" name="pd_FrpId" value="ICBC-NET-B2C" />工商银行
	<input type="radio" name="pd_FrpId" value="BOC-NET-B2C" />中国银行
	<input type="radio" name="pd_FrpId" value="ABC-NET-B2C" />农业银行
	<input type="radio" name="pd_FrpId" value="CCB-NET-B2C" />建设银行
	<input type="radio" name="pd_FrpId" value="BOCO-NET-B2C" />交通银行
</form>
<a id="pay" href="javascript:document.getElementById('form').submit();"></a>
  </body>
</html>
