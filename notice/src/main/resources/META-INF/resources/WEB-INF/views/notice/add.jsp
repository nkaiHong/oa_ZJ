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
<title>编辑公告</title>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-default">
		  <div class="panel-heading">编辑公告
		  </div>
		  <form action="${ctx }/notice" onsubmit="return checkContent();" method="post">
			  <div class="panel-body">
			  	
			  	 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			  	 <input type="hidden" name="id" value="${notice.id }"> 
			  	 
					  <div class="form-group">
					    <label for="inputTitle" class="col-sm-2 control-label">标题</label>
					    <div class="col-sm-10">
					      <input type="text" 
					      class="form-control" 
					      id="inputTitle" 
					      name="title"
					      required="required"
					      placeholder="公告的标题"
					      value="${notice.title }">
					    </div>
					  </div>
					 
						<div class="form-group">
					    <label for="inputType" class="col-sm-2 control-label">类型</label>
					    <div class="col-sm-10">
						      <select name="type_id"
						      required="required"
						      class="form-control">
					          	 <option value="">-- 请选择 --</option>
					          	 <%--循环得到下拉里面的值，也就是所选中的。 --%>
					          	<c:forEach items="${types }" var="t"> 
					          		<option value="${t.id }" ${notice.type.id eq t.id ? 'selected="selected"' : '' }>${t.name }</option>
					          	</c:forEach> 
					       
					          </select>
					    </div>
					  </div>
					  <div id="noticeContentEditor">${notice.content }</div>
					<textarea type="hidden" name="content" id="noticeContent" style="display: none;">${notice.content }</textarea>
				</div>
				<input type="hidden" name="content" id="noticeContent"/>
				按钮，保存的时候，要有状态（草稿、已发布、已撤回），阅读的时候必须是【已发布】的才能阅读。
		        <div class="panel-footer text-right">
	   	 			<button class="btn btn-primary">提交</button>
	  	 	    </div>
			  </div>
		  </form>
		</div>
	</div>
<script type="text/javascript" src="${ctx }/webjars/wangEditor/3.1.1/release/wangEditor.min.js"></script>
<script type="text/javascript">
	var E = window.wangEditor;
	//对应上面的id
	var editor = new E('#noticeContentEditor');
	// 隐藏网络图片引用tab
	//editor.customConfig.showLinkImg = false;
	
	// 显示图片上传的tab
	editor.customConfig.uploadImgServer = '${ctx}/storage/file/wangEditor';
	// 上传的时候，文件的字段名
    editor.customConfig.uploadFileName = 'file';
	 // 自定义上传的时候请求头内容
    editor.customConfig.uploadImgHeaders = {
   	    '${_csrf.headerName}': '${_csrf.token}'
   	};
	 
 	// 粘贴图片
    editor.customConfig.pasteIgnoreImg = false;
    // 不要过滤复制内容的样式，保持原本的样式，可能有些时候不能完全得到样式，此时可以自定义外观（写CSS）
    editor.customConfig.pasteFilterStyle = false;
	 
    // 接收改变后的内容，获取富文本编辑器里面的内容，放到#noticeContent里面
    editor.customConfig.onchange = function(html){
    	$("#noticeContent").val(html);
    };
	//创建编辑器
	editor.create();
	
	//检查公告内容是否输入
	var checkContent = function(){
		// .trim()去掉前后的空格
		var text = $("#noticeContentEditor").text().trim();
		if(text === ""){
			alert("公告内容必须填写！")
			return false;
		}
		
		return true;
	}
</script>
</body>
</html>