<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- application应用到整个项目 -->
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="application"></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>编辑用户信息</title>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		  <div class="panel-heading">编辑用户信息</div>
		   <div class="panel-body">
		   
		 	  <form class="form-horizontal"
	 	 		    action="${ctx }/identity/user"
			  	    method="post"
			  	    enctype="multipart/form-data">   <!-- 用于文件上传，或者是上传图片的 -->
			  	    
			  	    <input name="id" value="${user.id }" type="hidden"> 
			  	    <input type="hidden"
							name="${_csrf.parameterName}"
							value="${_csrf.token}"/>
							
			  	    <div class="col-sm-12 col-md-6">
					  <div class="form-group">
					    <label for="inputName" class="col-sm-2 control-label">姓名</label>
					    <div class="col-sm-10">
					      <input type="text" 
					      class="form-control" 
					      id="inputName" 
					      name="name"
					      required="required"
					      placeholder="用户的真实姓名"
					      value="${user.name }">
					    </div>
					  </div>
					 </div>
				  <div class="col-sm-12 col-md-6">
					  <div class="form-group">
					    <label for="inputLoginName" class="col-sm-2 control-label">登录名</label>
					    <div class="col-sm-10">
					      <input type="text" 
					      class="form-control" 
					      id="inputLoginName" 
					      name="loginName"
					      required="required"
					      
					      ${not empty user ? 'readonly = "readonly"' : ''}
					      placeholder="用于登录系统的账号"
					      value="${user.loginName }"/>
					    </div>
					  </div>
				  </div>
				  
				  <div class="col-sm-12 col-md-6">
					<div class="form-group ">
					    <label for="inputPassword" class="col-sm-2 control-label">密码</label>
					    <div class="col-sm-10">
					        <input type="password" 
					        	class="form-control" 
					        	id="inputPassword" 
					        	name="password"
					        	${empty user ? 'required = "required"' : ''}
					        	placeholder="用于登录系统的密码${not empty user ? '，不修改则不填写' : '' }"/>
					    </div>
					</div>
				  </div>
				  
				  
				  <div class="col-sm-12 ">
					<div class="form-group ">
					    <label for="inputPassword" class="col-sm-2 control-label">角色</label>
					    <div class="col-sm-10">
					    
					     	<div class="row">
					     		<div class="col-xs-5">已选择</div>
					     		<div class="col-xs-2"></div>
					     		<div class="col-xs-5">未选择</div>
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
					     		
					     		<div class="col-xs-5 roles unselect-roles">
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
				  				  
				 <div>
				 	   <div class="col-xs-12">
				 	   	 <div class="form-group">
				 	   	 	<div class="col-xs-12 text-right">
				 	   	 		<button type="submit" class="btn btn-primary">保存</button>
				 	   	 	</div>
				 	   	 </div>
				 	   </div>
				 </div>
			 </form>
		  </div>
	</div>
</div>
<script type="text/javascript" src="${ctx }/static/js/fkjava.js"></script>
</body>
</html>