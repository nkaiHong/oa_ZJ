<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>首页</title>
</head>
<body>
	
	<div class="col-sm-12 col-md-6 col-md-offset-3 text-center">
		<%-- 后面可以在这里写一些其他的内容，或者换一个其他的模块来实现首页 --%>
		<h1>公司公告</h1>
		${notice.title }
	</div>
</body>
</html>