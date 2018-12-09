<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fk" tagdir="/WEB-INF/tags" %>
<!-- application应用到整个项目 -->
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>流程定义管理模块</title>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		  <div class="panel-heading">流程定义
		  	 <div class="close">
		  	 	<a class="btn btn-default" data-toggle="modal" data-target="#uploadDefinitionFile">新增</a>
	  	     </div>	  	
		  </div>
		  <div class="panel-body">
		  <table class="table table-hover table-striped">
		  	<thead>
		  		<tr>	
		  			<th>分类</th>
		  			<th>名称</th>
		  			<th>KEY</th>
		  			<th>版本号</th>
		  			<th>操作</th>
		  		</tr>
		  	</thead>
		  	<tbody>
		  	<!-- 这里用到Spring为我们提供的分页page，page里面的有个content，spring帮我们把user的字段都封装到 -->
		  	<!-- content里面，这里直接调用就可以了 -->
		  		<c:forEach items="${page.content }" var="u">
		  		<tr>
		  			<td>${u.category }</td>
		  			<td>${u.name }</td>
		  			<td>${u.key }</td>
		  			<td>${u.version }</td>
		  			<td>
		  				<c:choose>
		  					<c:when test="${u.suspended }">
		  						<a href = "${ctx }/workflow/definition/active/${u.id}">激活</a>
		  					</c:when>
		  					<c:otherwise>
		  						<a href = "${ctx }/workflow/definition/disable/${u.id}">禁用</a>
		  					</c:otherwise>
		  				</c:choose>
					</td>
		  		</tr>
		  		</c:forEach>
		  	</tbody>
		  	<tfoot>
		  		<tr>
		  		   <td colspan="5" style="text-align: center;">
		  			<%-- 前缀随便写，关键要跟taglib指令的前缀要一致，冒号后面的则直接使用JSP Tag文件的名称 --%>
		  			<fk:page url="/workflow/definition?keyword=${param.keyword }" page="${page }"/>
				   </td>
		  		</tr>
		  	</tfoot>
		  </table>
		</div>
	</div>

<div class="modal fade" id="uploadDefinitionFile" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
  <form action="" method="post" enctype="multipart/form-data">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel">上传流程定义压缩文件</h4>
	      </div>
	      <div class="modal-body">
	        	请选择使用ZIP格式压缩流程定义所有的相关文件，包括bpmn、png、表单。建议直接在Eclipse中导出zip格式的压缩文件。<br/>
	        	<input name="file" type="file" required="required" />
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	        <button type="submit" class="btn btn-primary">上传</button>
	      </div>
	    </div>
	    <input type="hidden"
				name="${_csrf.parameterName}"
				value="${_csrf.token}"/>
   </form>
  </div>
</div>
</body>
</html>