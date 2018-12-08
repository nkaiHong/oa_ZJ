<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
 	<link rel="icon" href="${ctx }/images/favicon.ico">
    <title>智能OA--<sitemesh:write property="title"/></title>
    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="${ctx }/webjars/bootstrap/3.3.7/dist/css/bootstrap.min.css"/>
    <!-- Custom styles for this template -->
    
    <link rel="stylesheet" href="${ctx }/static/css/fkjava.css"/>
    <link rel="stylesheet" href="${ctx }/css/layout.css" />
	<script type="text/javascript" src="${ctx }/webjars/jquery/3.3.1/dist/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx }/webjars/bootstrap/3.3.7/dist/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		var contextPath ="${ctx}";
	</script>

		<%-- 把CSRF的验证码放到HTML头里面保存起来 --%>
		<%-- 使用AJAX的时候，必须要设置请求头，请求头的内容从HTML头里面获取 --%>
		<meta name="_csrf" content="${_csrf.token}"/>
		<meta name="_csrf_header" content="${_csrf.headerName}"/>
		
		<script type="text/javascript" src="${ctx }/static/js/fkjava.js"></script>
		
		<sitemesh:write property="head"/>
  </head>

  <body>
	<!-- 横幅导航 -->
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
		
			<%-- 此div是一个导航的头部显示的 --%>
			<div class="navbar-header">
				<%-- navbar-toggle collapsed : 在正常大小的屏幕中，此按钮是隐藏的，只有在小屏幕的时候才显示出来 --%>
				<%-- data-target="#navbar"  此按钮显示出来以后，用于【显示/隐藏菜单】 --%>
				
				<button type="button" 
					class="navbar-toggle collapsed" 
					data-toggle="collapse" 
					data-target="#navbar" 
					aria-expanded="false" 
					aria-controls="navbar">
					
					<%-- class="sr-only" 表示用于给屏幕阅读器使用的 --%>
					<span class="sr-only">显示或隐藏导航</span>
					<span class="glyphicon glyphicon-align-justify" style="color: white;"></span>
				</button>
				
				
				<button type="button" class="navbar-toggle collapsed" data-toggle="sidebar">
					<span class="sr-only">显示或隐藏菜单</span>
					<span class="glyphicon glyphicon-th-list" style="color: white;"></span>
				</button>
				
				<a class="navbar-brand" href="index.html#">智能OA</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="index.html#">首页</a></li>
					<li><a href="index.html#">设置</a></li>
					<li><a href="index.html#">个人</a></li>
					<li><a href="index.html#">帮助</a></li>
					<li><a href="${ctx }/layout/person"><div class="glyphicon glyphicon-user" style="color: white "></div></a></li>
					<li><a href="#" onclick="$('#logout-form').submit();">退出</a></li>
				</ul>
				
				<%-- 隐藏的退出功能的表单，退出必须要POST方式提交 --%>
				<form id="logout-form" action="${ctx }/security/do-logout" method="post" style="display: none;">
						<input type="hidden"
						name="${_csrf.parameterName}"
						value="${_csrf.token}"/>
				</form>
				
				
				<!-- 搜索框可以留着，因为可以让所有的页面都使用相同的搜索框 -->
				<form class="navbar-form navbar-right" method="get" action="">
					<input type="text" class="form-control" placeholder="输入关键字,按回车进行搜索..." name="keyword" value="${param.keyword }">
				</form>
			</div>
		</div>
	 </nav>

	<div class="container-fluid">
		<div class="row"> 
			<!-- 一级、二级菜单显示的地方 -->
			<div class="col-sm-3 col-md-2 sidebar" id="left-sidebar">
			</div>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<sitemesh:write property="body"/>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctx }/js/layout.js"></script>
  </body>
</html>
