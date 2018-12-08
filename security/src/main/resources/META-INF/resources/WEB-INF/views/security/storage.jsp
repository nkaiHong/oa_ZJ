<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>文件上传</title>
<style type="text/css">
.but1{
	margin-top: -50px;
	margin-left: 170px;
}
</style>
</head>
<body>
	<div>
		<form action="" method="post" enctype="multipart/form-data">
			<div>
			<label for="exampleInputFile">文件上传</label>
			<input type="file" name="file"/>
			<button type="submit" class="btn btn-primary but1">上传</button>
			<input type="hidden"
							name="${_csrf.parameterName}"
							value="${_csrf.token}"/>
			</div>
		</form>
		
	</div>
</body>
</html>