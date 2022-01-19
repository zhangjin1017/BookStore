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
    <base target="body">
    <title>Administrator Left</title>
    
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
	background:url(<c:url value='/images/adminbg.jpg' />);
	margin:0px;
}
.title {
	text-align:center;
	background:url(<c:url value='/images/admintopbg.jpg' />);
	font-family:"楷体";
	border:2px solid purple;
	color:fuchsia;
}

dl {
	background:url(<c:url value='/images/admintopbg.jpg' />);
	text-align:center;
	border:2px solid purple;
	height:32px;
}

dt {
	padding:5px;
	border:1px solid purple;
	font-family:"仿宋";
	font-size:20px;
	color:fuchsia;
}

dd {
	margin:0px;
	padding:5px;
	height:20px;
	border:1px solid purple;
}

a {
	text-decoration: none;
	color:purple;
}
a:HOVER {
	color:silver;
}

.open {
	overflow:visible;
}

.close {
	overflow:hidden;
}

</style>
  </head>
  
  <body>
<div class="title">
	<h2>游子书屋管理栏</h2>
</div>
<dl class="close">
	<dt onclick="list(this)">分类管理</dt>
	<dd><a href="<c:url value='/admin/AdminCategoryServlet?method=findAll' />" target="body">查看分类</a></dd>
	<dd><a href="<c:url value='/adminjsps/admin/category/add.jsp' />" target="body">添加分类</a></dd>
</dl>
<dl class="close">
	<dt onclick="list(this)">图书管理</dt>
	<dd><a href="<c:url value='/admin/AdminBookServlet?method=findAll' />" target="body">查看图书</a></dd>
	<dd><a href="<c:url value='/admin/AdminBookServlet?method=addPre' />" target="body">添加图书</a></dd>
</dl>
<dl class="close">
	<dt onclick="list(this)">订单管理</dt>
	<dd><a href="<c:url value='/admin/AdminOrderServlet?method=findAllOrder' />" target="body">所有订单</a></dd>
	<dd><a href="<c:url value='/admin/AdminOrderServlet?method=findByState&state=1' />" target="body">未付款订单</a></dd>
	<dd><a href="<c:url value='/admin/AdminOrderServlet?method=findByState&state=2' />" target="body">已付款订单</a></dd>
	<dd><a href="<c:url value='/admin/AdminOrderServlet?method=findByState&state=3' />" target="body">未收货订单</a></dd>
	<dd><a href="<c:url value='/admin/AdminOrderServlet?method=findByState&state=4' />" target="body">已完成订单</a></dd>
</dl>
  </body>
  
  <script type="text/javascript">
  	
	function list(node) {
		var nodeParent = node.parentNode;
		var collDlNodes = document.getElementsByTagName("dl");
		for(var i = 0; i < collDlNodes.length; i++) {
			var oDlNode = collDlNodes[i];
			if(nodeParent == oDlNode) {
				if(oDlNode.className == "close") {
					oDlNode.className = "open";
					var collDdNodes = oDlNode.getElementsByTagName("dd");
					var h = collDdNodes.length * 32 + 32;
					oDlNode.style = "height:"+ h;
				} else {
					oDlNode.className = "close";
					oDlNode.style = "height:32px";
				}
			} else {
				oDlNode.className = "close";
				oDlNode.style = "height:32px";
			}
		}
		
	}
  
  </script>
  
</html>
