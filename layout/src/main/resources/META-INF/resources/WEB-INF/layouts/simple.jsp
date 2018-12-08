<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="application"></c:set>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" con tent="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="${ctx }/images/favicon.ico">
    <title>智能OA ---<sitemesh:write property="title"/></title>
    <!-- Bootstrap core CSS -->
    
    <link rel="stylesheet" href="${ctx }/webjars/bootstrap/3.3.7/dist/css/bootstrap.min.css"/>
    <!-- Custom styles for this template -->
    
    <link rel="stylesheet" href="${ctx }/static/css/fkjava.css"/> 
    <link rel="stylesheet" href="${ctx }/css/layout.css" />
	<script type="text/javascript" src="${ctx }/webjars/jquery/3.3.1/dist/jquery.min.js"></script>
	
	<script type="text/javascript" src="${ctx }/webjars/bootstrap/3.3.7/dist/js/bootstrap.min.js"></script>



	<sitemesh:write property="head"/>
  </head>

  <body>
	
	
		<div class="container-fluid">
			<sitemesh:write property="body"/>
		</div>
	
  </body>
</html>
