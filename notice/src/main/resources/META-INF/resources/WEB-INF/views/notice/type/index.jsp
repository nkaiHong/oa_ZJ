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
<title>公告类型管理</title>
<style type="text/css">
.type:hover
{
	background-color: #aaa;
	cursor: pointer;
}
.type-form
{
	border-left: 1px solid #faa;
	padding-bottom: 10px;
}
.type
{
	padding-right: 0px;
}
.remove-btn
{
	background-color: #fff;
	border: 1px solid green;
	padding-left: 5px;
	padding-right: 5px; 
}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		  <div class="panel-heading">公告类型管理
		  	 <div class="close">
		  	 	<a class="btn btn-default">新增</a>
	  	     </div>	  	
		  </div>
		  <div class="panel-body">
		  	<!-- 公告列表 -->
		  	<div class="col-sm-12 col-md-4">
		  		<c:forEach items="${types }" var="t">
		  		<!-- 自定义属性，拿过角色里面的所有信息，id，name，typeKey -->
		  			<div class="col-xs-12 type" 
		  			data-id="${t.id }"
		  			data-name="${t.name }">
		  				${t.name }
		  				<span class="pull-right hide remove-btn">x</span>
		  			</div>
		  		</c:forEach>
		  	</div>
		  	<div class="col-sm-12 col-md-8 type-form">
		  		 <form class="form-horizontal"
	 	 		    action=""
			  	    method="post">  
			  	     
			  	  	<input type="hidden" name="id">
			  	  	<%-- 当用户打开表单的时候，生成一个随机的验证码存储在表单里面 --%>
					<%-- 提交的时候，会判断Session里面是否有随机验证码，并且要求浏览器提交过来的随机验证码要相同 --%>
			  	  	<input type="hidden"
							name="${_csrf.parameterName}"
							value="${_csrf.token}"/>
				
			  	    <div class="col-sm-12">
					  <div class="form-group">
					    <label for="inputName" class="col-sm-2 control-label">类型名称</label>
					    <div class="col-sm-10">
					      <input type="text" 
					      class="form-control" 
					      id="inputName" 
					      name="name"
					      required="required"
					      placeholder="公告类型名称">
					    </div>
					  </div>
					 </div>
				 <div>				
		 	   	 	<div class="col-sm-12 text-right">
		 	   	 		<button class="btn btn-primary">保存</button>
		 	   	 	</div>		
				 </div>
			 </form>
		  	</div>
		  </div>
	</div>
	<script type="text/javascript">
$(function(){
	$(".type").click(function(){
		// 发生事件的div
		var div = $(this);
		var id = div.attr("data-id");
		var name = div.attr("data-name");
		var typeKey = div.attr("data-typeKey");
		
		$(".type-form [name='id']").val(id);
		$(".type-form #inputName").val(name);
		$(".type-form #inputtypeKey").val(typeKey);
	});
	
	$(".type").hover(function(){
		var span = $("span",this);
		//事件发生的时候，会删除hide类，
		//鼠标移开的时候，会自动加上hide类
		span.toggleClass("hide");
	})
	
	$(".remove-btn").click(function(event){
		//先阻止事件的传播，因为每一次的点击都会跟之前的点击事件重复
		event.stopPropagation();
		//parent 这里拿到的父级就是div，因为div里面有id，而删除就是根据id来删除的
		var div = $(this).parent();
		var id = div.attr("data-id");
		var url = "${ctx}/notice/type/" + id;
		// 发生DELETE请求删除数据
		// DELETE请求，表示删除URL对应的资源，浏览器不能直接发送
		// 有两种方式发送：使用AJAX发送、使用POST表单模拟发送（Spring MVC扩展的，需要一个名为_method的参数，值为DELETE）
		$.ajax({
			url:url,
			method:"delete",//发送delete请求
			//data是返回的数据
			success:function(data,status,xhr){
				//删除成功
				//相当于是重定向,重定向到主页
				/* document.location.href="${ctx}/notice/type"; */
				window.location.reload();
			},
			error:function(data,status,xhr){
				//responseJSON表示返回的JSON对象
				//message是提示信息
				alert(data.responseJSON.message);
			}
		})
	})
});
</script>
</body>
</html>