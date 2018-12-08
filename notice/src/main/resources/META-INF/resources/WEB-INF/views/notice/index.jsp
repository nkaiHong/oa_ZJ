<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fk" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- application应用到整个项目 -->
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="application"></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<style type="text/css">
.unread{
	font-weight: bold;
}
</style>
<title>公告管理</title>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		
		  <div class="panel-heading">公告管理
		  	 <div class="close">
		  	 	<a class="btn btn-default" href="${ctx }/notice/add">新增</a>
	  	     </div>	  	
		  </div>
		  <div class="panel-body">
			  	<table class="table table-hover table-striped">
			  	<thead>
			  		<tr>	
			  			<th>标题</th>
			  			<th>作者</th>
			  			<th>撰写时间</th>
			  			<th>状态</th>
			  			<th>操作</th>
			  		</tr>
			  	</thead>
			  	<tbody>
			  		<c:forEach items="${page.content }" var="p">
			  			<tr class="${(p.notice.status eq 'RELEASED' and empty p.readTime) ? 'unread':''}">
			  				<td>${p.notice.title }</td>
			  				<td>${p.notice.author.name }</td>
			  				<td><fmt:formatDate value= "${p.notice.writeTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			  				<td>
			  					<c:choose>
			  						<c:when test="${p.notice.status eq 'DRAFT' }">草稿</c:when>
			  						<c:when test="${p.notice.status eq 'RECALL' }">撤回</c:when>
			  						<c:when test="${p.notice.status eq 'RELEASED' }">已发布</c:when>
			  					</c:choose>
			  				</td>
			  				<td>
			  					<%-- 发布按钮：草稿状态可以发布 --%>
								<%-- 编辑：草稿状态可以编辑 --%>
								<%-- 删除：草稿状态可以删除 --%>
								<!-- 草稿 -->
								<c:if test="${p.notice.status eq 'DRAFT' }">
									<a class="btn btn-xs btn-primary" onclick="publishNotice('${p.notice.id}')">发布</a>
									<a class="btn btn-xs btn-default" onclick="editNotice('${p.notice.id}')">编辑</a>
									<a class="btn btn-xs btn-danger" onclick="deleteNotice('${p.notice.id}')">删除</a>
								</c:if>
								
								<%-- 撤回：当前用户编写的、已经发布的可以撤回 --%>
								<%-- 阅读：发布状态可以阅读 --%>
								<!-- 已发布 -->
								<c:if test="${p.notice.status eq 'RELEASED' }">
									<%-- 当前用户编写的才能撤回 --%>
									<c:if test="${p.notice.author.id eq sessionScope['SPRING_SECURITY_CONTEXT'].authentication.principal.userId }">
										<a class="btn btn-xs btn-warning" onclick="recallNotice('${p.notice.id}')">撤回</a>
									</c:if>
									<a class="btn btn-xs btn-info" onclick="readNotice('${p.notice.id}')">阅读</a>
								</c:if>
			  				</td>
			  			</tr>
			  		</c:forEach>
			  	</tbody>
			  	<tfoot>
		  		<tr>
		  		   <td colspan="5" style="text-align: center;">
		  			<%-- 前缀随便写，关键要跟taglib指令的前缀要一致，冒号后面的则直接使用JSP Tag文件的名称 --%>
		  			<fk:page url="/notice?keyword=${param.keyword }" page="${page }"/>
				   </td>
		  		</tr>
		  	</tfoot>
		  </div>
		</div>
	</div>
<script type="text/javascript">
	var publishNotice  = function(id){
		document.location.href="${ctx}/notice/publish/" + id;
	}
	
	var editNotice = function(id){
		document.location.href="${ctx}/notice/edit/" + id;
	}
	
	var deleteNotice = function(id){
		$.ajax({
			url: "${ctx}/notice/" + id,
			method: "delete",
			success: function(){
				// 重新加载页面
				document.location.reload();
			}
		});
	};
	var recallNotice = function(id){
			document.location.href="${ctx}/notice/recall/" + id;
		}
		
	var readNotice = function(id){
		document.location.href="${ctx}/notice/" + id;
	}
</script>
</body>
</html>