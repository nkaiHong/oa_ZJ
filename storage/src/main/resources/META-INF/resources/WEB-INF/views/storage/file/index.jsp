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
<title>文件管理</title>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		  <div class="panel-heading">文件管理
		  	 <div class="close">
		  	 	<a class="btn btn-default" data-toggle="modal" data-target=".file-upload-dialog">新增</a>
	  	     </div>	  	
		  </div>
		  <div class="panel-body">
		  <table class="table table-hover table-striped">
		  	<thead>
		  		<tr>	
		  			<th>文件名</th>
		  			<th>文件类型</th>
		  			<th>文件大小</th>
		  			<th>上传时间</th>
		  			<th>操作</th>
		  		</tr>
		  	</thead>
		  	<tbody>
		  	<!-- 这里用到Spring为我们提供的分页page，page里面的有个content，spring帮我们把user的字段都封装到 -->
		  	<!-- content里面，这里直接调用就可以了 -->
		  		<c:forEach items="${page.content }" var="u">
		  			<tr>
		  				<td>${u.name}</td>
		  				<td>${u.contentType}</td>
		  				<td>${u.fileSize}</td>
		  				<td>${u.uploadTime }</td>
		  				<td>
		  					<a class="label label-success" href="${ctx }/storage/file/${u.id}">下载</a> &nbsp;&nbsp;
		  					<a class="label label-danger" href="javascript:deleteFile('${u.id }')">删除</a>
		  				</td>
		  			</tr>
		  		</c:forEach>
		  	</tbody>
		  	<tfoot>
		  		<tr>
		  		   <td colspan="5" style="text-align: center;">
		  			<%-- 前缀随便写，关键要跟taglib指令的前缀要一致，冒号后面的则直接使用JSP Tag文件的名称 --%>
		  			<fk:page url="/storage/file?keyword=${param.keyword }" page="${page }"/>
				   </td>
		  		</tr>
		  	</tfoot>
		  </table>
		</div>
	</div>
		<div class="modal fade file-upload-dialog" tabindex="-1" role="dialog">
		  <div class="modal-dialog" role="document">
		   	  <div class="modal-content">
		   	  <form action="" method="post" enctype="multipart/form-data">
		   	  
		   	  	  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		   	  	  
			      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal"
				         aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h4 class="modal-title">上传文件</h4>
			      </div>
			      
			      <div class="modal-body">
			        	<p>请选择要上传的文件，文件大小不能超过10M。</p>
			        	<div class="form-group">
						    <label for="uploadFile">选择文件</label>
						    <input type="file" id="uploadFile" name="file" required="required">
						    <p class="help-block">自己上传的文件，只有自己能够看见。</p>
						</div>
			      </div>
			      
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			        <input type="hidden" name="MAX_FILE_SIZE" value="10485760" />
			        <button type="submit" class="btn btn-primary submits">上传</button>
			     </div>
		     </form>
		     </div>
		</div>
	</div>
	
	<script type="text/javascript">
		var deleteFile = function(id){
			$.ajax({
				url: "${ctx}/storage/file/" + id,
				method: "DELETE",
				success: function(){
					//重新加载页面
					window.location.reload();
				}
			})
		}
	</script>
</body>
</html>