<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<center>
		<h3>个人信息</h3>
	</center>
	<table class="table table-bordered table-hover" style="cursor: pointer;">
		<thead>
				<tr>
					<td class="active">姓名</td>
					<td class="success">用户名</td>
					<td class="info">拥有的角色</td>
				<tr>
		</thead>
		<tbody>
			<c:forEach items="${page.content }" var="u">
			<tr>
				<td class="active">${u.name}</td>
				<td class="success">${u.loginName}</td>
				<td class="info">${u.roles}</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>