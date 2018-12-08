<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fk" tagdir="/WEB-INF/tags" %>
<!-- application应用到整个项目 -->
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="application"></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>用户菜单模块</title>	
<%-- 所有放到static、public、resources里面的文件，都是在根目录的 --%>
<link rel="stylesheet" href="${ctx }/zTree/css/zTreeStyle/zTreeStyle.css"> 
<script type="text/javascript" src="${ctx }/zTree/js/jquery.ztree.all.min.js"></script><%-- async="async"表示异步加载js --%>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		  <div class="panel-heading">菜单管理
		  	 <div class="close">
		  	 	<a class="btn btn-default">新增</a>
	  	     </div>	  	
		  </div>
		<div class="panel-body">
			<div class="col-xs-12 col-sm-4 tree-container">
		  	<ul id="treeDemo" class="ztree"></ul>
		  	</div>

		
			<div class="col-xs-12 col-sm-8 form-container select-role-form">
				<form action="" method="post" class="form-hontainer">
					<input type="hidden" name="id" id="id"/><!-- 这里的id值必须加上，不然会出现bug！！！ -->
					
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					
					 <div class="col-sm-12">
					  <div class="form-group">
					    <label for="inputName" class="col-sm-2 control-label">上级菜单</label>
					    <div class="col-sm-10">
					     	<span id="parentName"></span>
					     	<input id="parentId" name="parent.id" type="hidden">
					    </div>
					  </div>
					 </div>
					 
					 
					  <div class="col-sm-12">
					  <div class="form-group">
					    <label for="inputName" class="col-sm-2 control-label">菜单名称</label>
					    <div class="col-sm-10">
					      <input type="text" 
					      class="form-control" 
					      id="inputName" 
					      name="name"
					      required="required"
					      placeholder="菜单">
					    </div>
					  </div>
					 </div>
					 
					 <div class="col-sm-12">
					  <div class="form-group">
					    <label for="inputName" class="col-sm-2 control-label">URL</label>
					    <div class="col-sm-10">
					      <input type="text" 
					      class="form-control" 
					      id="inputURL" 
					      name="url"
					      required="required"
					      placeholder="访问菜单的时候使用URL">
					    </div>
					  </div>
					 </div>
					 
					 <div class="col-sm-12">
					 	<div class="form-group">
					 		<label for="inputURL" class="col-sm-2 control-label">类型</label>
					 		<div class="col-sm-10">
					 			<div class="radio">
								  <label>
								    <input type="radio" name="type" value="LINK">
								  	 链接类型的菜单，用于显示出来给用户点击使用的
								  </label>
								</div>
								<div class="radio">
								  <label>
								    <input type="radio" name="type" value="BUTTON">
								   	 用于在主页面中，作为用户是否有权限操作此按钮
								  </label>
								</div>
					 		</div>
					 	</div>
					 </div>
					 
					 
					  <div class="col-sm-12 ">
					<div class="form-group ">
					    <label class="col-sm-2 control-label">角色</label>
					    <div class="col-sm-10">
					    
					     	<div class="row">
					     		<div class="col-xs-5">已选择</div>
					     		<div class="col-xs-2"></div>
					     		<div class="col-xs-5 wei">未选择</div>
					     	</div>
					     	<div class="row">
					     		<div class="col-xs-5 roles selected-roles">
					     			<ul>
					     				<c:forEach items="${user.roles }" var="r">
					     					<c:if test="${ not r.fixed }">
							     				<li>
							     					<label>
							     						<input type="checkbox" name="roles[0].id" value="${r.id }"/>
							     						${r.name }
							     					</label>
							     				</li>
					     					</c:if>
					     				</c:forEach>
					     			</ul>
					     		</div>
					     		
					     		<div class="col-xs-2">
					     			<a class="btn btn-info add-selected">添加所选</a>
					     			<a class="btn btn-info add-all">添加全部</a>
					     			<a class="btn btn-danger remove-selected">移除所选</a>
					     			<a class="btn btn-danger remove-all">移除全部</a>
					     		</div>
					     		
					     		<div class="col-xs-5 roles unselect-roles un">
					     			<ul>
					     				<c:forEach items="${roles }" var="r">
					     				<%-- 因为重写了equals和hashCode，才可以使用contains判断r是否在user里面 --%>
					     					<c:if test="${ not user.roles.contains(r) }">
							     				<li>
							     					<label>
							     						<input type="checkbox" name="roles[0].id" value="${r.id }"/>
							     						${r.name }
							     					</label>
							     				</li>
					     					</c:if>
					     				</c:forEach>
					     			</ul>
					     		</div>
					     	</div>
					    </div>
					</div>
				  </div>
				  
		  			<div class="col-xs-12 text-right but">
		  				<button class="btn btn-default reset-button" type="button">重置</button>
		 	   	 		<button class="btn btn-primary">保存</button>
		 	   	 	</div>
				</form>
			</div>
		
		</div>
	</div>
<script type="text/javascript" src="${ctx }/static/js/fkjava.js"></script>
<script type="text/javascript">
	var moveURL = "${ctx}/menu/move";
	var loadURL = "${ctx}/menu";
	var removeURL = "${ctx}/menu";
</script>
<script type="text/javascript" src="${ctx }/js/menu.js"></script>
</body>
</html>