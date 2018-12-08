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
<title>阅读公告</title>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		  <div class="panel-heading">阅读公告
		  </div>
		 
			  <div class="panel-body">
			  	
			  	 <h1 class="text-center">${notice.title }</h1>
			  	 作者:${notice.author.name } 发布时间:<fmt:formatDate value="${notice.releaseTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				<hr/>
				${notice.content }
				</div>
				<div class="panel-footer text-right">
					<%-- 如果同时还查询了阅读记录，那么可以在已读之后不要显示【已阅】按钮，而是显示在什么时候已读。 --%>
					<a class="btn btn-primary " onclick="readNotice('${notice.id}')">已阅</a>
				</div>
		</div>
	</div>
<script type="text/javascript" src="${ctx }/webjars/wangEditor/3.1.1/release/wangEditor.min.js"></script>
<script type="text/javascript">
	var readNotice = function(id){
		$.ajax({
			url: "${ctx}/notice/" + id,
			method: "post",
			dataType: "json",
			success: function(){
				document.location.href="${ctx}/notice";
				}
		})
	}
</script>
</body>
</html>