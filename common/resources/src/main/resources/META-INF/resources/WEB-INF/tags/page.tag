<%@ tag language="java" pageEncoding="utf-8"%>
<!-- 要用到这个文件，必须要在固定的路径下， /WEB-INF/tags -->
<%-- 这里可以写JSP的任何代码，并且可以定义接收参数！ -->
<%-- tag指令是JSP 2.0的时候提供的一个标签库指令，把HTML内容作为标签库来使用，避免在Java代码嵌入HTML -->
<%-- attribute指令用于在JSP Tag文件中定义参数 --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- application应用到整个项目 -->
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="application"></c:set>
<%@ attribute name="url" required="true" type="java.lang.String"%>
<%@ attribute name="page" required="true" type="org.springframework.data.domain.Page"%>
<c:if test="${not empty page and page.totalPages ne 0}">
<%-- 判断如果url包含了问号，那么后面加上&符号，然后拼pageNumber参数 --%>
<c:if test="${url.indexOf('?') >= 0 }">
		<c:set var="url" value="${ctx }${url }&pageNumber="/>
</c:if>

<%-- 否则直接在后面拼问号，加pageNumber --%>

<c:if test="${url.indexOf('?') < 0 }">
		<c:set var="url" value="${ctx }${url }?pageNumber="/>
</c:if>
<nav aria-label="分页导航">
	  <ul class="pagination">
	    <li>
	    <!-- 分页的时候。，在上一页中，最低的页码应该是0，如果是负数，则会报错。 eq 就是等于的意思 -->	
	    <!-- 这里的判断则是当点击上一页的时候，如果小于0 的时候，就设置为0，否则就是当前页码 将去1，因为分页的页码是从0开始的，而显示是从一开始的，那就 -->											
	      <a href="${url}${page.number eq 0 ? 0 : page.number - 1}" aria-label="上一页">
	        <span aria-hidden="true">&laquo;</span>
	      </a>
	    </li>
	    <c:set var="begin" value="${page.number - 2 }"></c:set>
	    <c:set var="end" value="${page.number + 2 }"></c:set>
	    
	    <!-- 向左边移动的时的问题，页码不能是负数 ，最少是0,也就是第一页-->
	    <!-- lt 就是小于的意思 -->
	    <c:if test="${begin lt 0 }">
	    	<!-- 此时begin是负数，在begin之前加上减号，就变成正数 -->
	    	<!-- end + begin取反，其实就是把end往右边挪一点 -->‘
	    	<c:set var="end" value="${end + (-begin) }"></c:set>
	    	<c:set var="begin" value="0"></c:set>
	    </c:if>
	    
	    
	    <!-- 向右边挪的问题，页码不能超过 总页数 -->
	    <!-- gt 就是大于的意思 -->
	    <c:if test="${end gt (page.totalPages - 1) }">
	    	<!-- 最大的页码不能超过总页数 -->
	    	<c:set var="end" value="${page.totalPages - 1 }"></c:set>
	    	<c:set var="begin" value="${end - 4 }"></c:set>
	    </c:if>
	    
	    <!-- 最小的页码的为0，如果还小于0，就直接设置为0，因为不能为扶负数 -->
	    <c:if test="${begin lt 0 }">
	    	<c:set var="begin" value="0"></c:set>
	    </c:if>
	    
	    
	   	<c:forEach begin="${begin }" end="${end }" var="number">
	    <li class="${page.number eq number ? 'active' : ''}"><a href="${url}${number}">${number+1 }</a></li>
	    </c:forEach>
	    <li>
	    <!-- 在下一页的时候，如果过最大的页码数，则设置为最大的页码数，不能继续加1向右移动 -->
	      <a href="${url}${page.number ge (page.totalPages - 1) ? page.totalPages - 1 : page.number + 1}" aria-label="下一页">
	        <span aria-hidden="true">&raquo;</span>
	      </a>
	    </li>
	  </ul>
</nav>
</c:if>
<c:if test="${empty page or page.totalPages eq 0}">
	没有任何数据
</c:if>