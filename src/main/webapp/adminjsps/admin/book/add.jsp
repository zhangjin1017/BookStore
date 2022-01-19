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
    
    <title>My JSP 'add.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
.textInput {
	width:150px;
	height:20px;
}
</style>
  </head>
  
  <body>
<h1>添加图书</h1>
<p style="color:red; font-weight:900">${requestScope.msg }</p>
<form action="<c:url value='/admin/AdminAddBookServlet' />" method="post" enctype="multipart/form-data">
图书名称：<input type="text" name="bname" class="textInput" /><br/>
图书图片：<input type="file" name="image" class="textInput" /><br/>
图书单价：<input type="text" name="price" class="textInput" /><br/>
图书作者：<input type="text" name="author" class="textInput" /><br/>
图书分类：<select style="width:150px; height:20px;" name="cid">
			<c:forEach var="category" items="${requestScope.categoryList }">
				<option value="${pageScope.category.cid }">${pageScope.category.cname }</option>
			</c:forEach>
		</select><br/>
		<input type="submit" value="添加图书" />
</form>
  </body>
</html>
