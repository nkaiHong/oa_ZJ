<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 引用JSP Tag文件 --%>
<%@ taglib prefix="fk" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>启动流程</title>
</head>
<body>
<div class="container-fluid">
	<c:if test="${not empty result }">
		<div class="alert alert-danger" role="alert">${result.message }</div>
	</c:if>
	<c:if test="${form.definition.suspended }">
		<div class="alert alert-danger" role="alert">流程处于停用状态，暂时不能使用</div>
	</c:if>
<form action="./${form.definition.id }?processDefinitionKey=${form.definition.key }"  
	method="post" enctype="multipart/form-data">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			启动【${form.definition.name }】流程
		</div>
		<div class="panel-body">
		
			<c:if test="${not empty form.definition.description }">
				<div class="alert alert-info" role="alert">${form.definition.description }</div>
			</c:if>
			
			<p>
			<%-- 业务私有的部分开始 --%>
			${form.content }
			<%-- 业务私有的部分结束 --%>
			</p>
			
			<fieldset>
				<legend>备注</legend>
				<textarea name="remark" class="form-control" rows="10" required="required"></textarea>
			</fieldset>
		</div>
			<div class="panel-footer text-right">
				<c:if test="${form.definition.suspended }">
					<button type="button" class="btn btn-primary disabled">暂停使用</button>
				</c:if>
				<c:if test="${not form.definition.suspended }">
					<button type="submit" class="btn btn-primary">提交</button>
				</c:if>
			</div>
	</div>
	<input type="hidden"
			name="${_csrf.parameterName}"
			value="${_csrf.token}"/>
</form>
</div>
</body>
</html>