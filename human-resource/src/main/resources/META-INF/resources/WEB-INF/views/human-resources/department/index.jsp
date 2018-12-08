<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fk" tagdir="/WEB-INF/tags" %>
<!-- application应用到整个项目 -->
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>部门管理模块</title>	
<%-- 所有放到static、public、resources里面的文件，都是在根目录的 --%>
<link rel="stylesheet" href="${ctx }/zTree/css/zTreeStyle/zTreeStyle.css"> 
<script type="text/javascript" src="${ctx }/zTree/js/jquery.ztree.all.min.js"></script><%-- async="async"表示异步加载js --%>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		  <div class="panel-heading">部门管理
		  </div>
		<div class="panel-body">
			<div class="col-xs-12 col-sm-4 tree-container">
		  	<ul id="departmentTree" class="ztree"></ul>
		  	</div>

		
			<div class="col-xs-12 col-sm-8 form-container select-role-form">
				<form action="" method="post" class="form-horizontal">
					<input type="hidden" name="id" id="id" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<div class="col-sm-12">
						<div class="form-group">
						    <label class="col-sm-2 control-label">上级部门</label>
						    <div class="col-sm-10">
						        <span id="parentName"></span>
								
						    </div>
						</div>
					</div>
					<div class="col-sm-12">
						<div class="form-group">
						    <label for="inputName" class="col-sm-2 control-label">部门名称</label>
						    <div class="col-sm-10">
						        <input type="text" 
						        	class="form-control" 
						        	id="inputName" 
						        	name="name"
						        	required="required"
						        	placeholder="部门名称"/>
						    </div>
						</div>
					</div>
					<div class="col-sm-12">
						<div class="form-group">
						  <label for="selectManager" class="col-sm-2 control-label">部门经理</label>
						    <div class="col-sm-10">
						        <input type="text" 
						        	class="form-control" 
						        	id="selectManager"/>
						        <input type="hidden" 
						        	id="managerUserId"
						        	name="manager.user.id" />
						    </div>
						</div>
					</div>
					<div class="col-sm-12 text-right">
						<button class="btn btn-default reset-button" type="button">重置</button>
						<button class="btn btn-primary">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="${ctx }/webjars/devbridge-autocomplete/1.4.8/dist/jquery.autocomplete.min.js"></script>
<script type="text/javascript" src="${ctx }/js/department.js"></script>


</body>
</html>