<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>文件列表</title>
<style type="text/css">
.but1{
	margin-top: -50px;
	margin-left: 170px;
}
</style>
</head>
<body>
	<form action="" method="post" enctype="multipart/form-data" >
			<div>
			<label for="exampleInputFile">文件上传</label>
			<input type="file" name="file" required="required"/>
			<button type="submit" class="btn btn-primary but1">上传</button>
			<input type="hidden"
							name="${_csrf.parameterName}"
							value="${_csrf.token}"/>
			</div>
	</form>
	
	<div>
		<table class="table table-striped table-bordered table-hover" style="cursor: pointer;"> 
			<thead>
				<tr>
					<th>文件名</th>
					<th>文件类型</th>
					<th>文件大小</th>
					<th>下载</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.content }" var="u">
				<tr>
					<td>${u.name }</td>
					<td>${u.contentType }</td>
					<td>${u.fileSize }</td>
					<td>
						<a href="${pageContext.request.contextPath }/security/list/${u.id}">下载</a>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>