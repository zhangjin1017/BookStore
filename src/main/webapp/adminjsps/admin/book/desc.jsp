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
		width:220px;
		height:145px;
		text-align:center;
	}
	img {
		margin:10px;
	}
	.trim {
		margin:0px 10px 10px 0px;
	}
	.btn {
		margin:0px 0px 0px 40px;
	}
</style>

<script type="text/javascript">
	function setMethod(method) {
		var ele = document.getElementById("method");
		ele.value = method;
	}
</script>

  </head>
  
  <body>
<div class="divv">
	<img src="<c:url value='/${requestScope.book.image }' />" border="0" />
</div>
<form style="margin:20px" id="form" action="<c:url value='/admin/AdminBookServlet' />" method="post">
			<input type="hidden" name="method" id="method" />
			<input type="hidden" name="bid" value="${requestScope.book.bid }" />
			<input type="hidden" name="image" value="${requestScope.book.image }" />
	图书名称：<input class="trim" type="text" name="bname" value="${requestScope.book.bname }" /><br/>
	图书单价：<input class="trim" type="text" name="price" value="${requestScope.book.price }" />元<br/>
	图书作者：<input class="trim" type="text" name="author" value="${requestScope.book.author }" /><br/>
	图书分类：<select class="trim" style="width:150px; height:20px;" name="cid">
				<c:forEach var="category" items="${requestScope.categoryList }">
					<c:choose>
						<c:when test="${requestScope.book.category.cid eq pageScope.category.cid }">
							<option selected="selected" value="${pageScope.category.cid }">${pageScope.category.cname }</option>
						</c:when>
						<c:otherwise>
							<option value="${pageScope.category.cid }">${pageScope.category.cname }</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select><br/>
			<input class="btn" type="submit" value="删除" onclick="setMethod('delete')" />
			<input class="btn" type="submit" value="编辑" onclick="setMethod('edit')" />
</form>
  </body>
</html>
