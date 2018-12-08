package org.fkjava.security.service.impl;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

public class MyAccessControl {

	private static final Logger LOG = LoggerFactory.getLogger(MyAccessControl.class);
	public boolean check(Authentication authentication,HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		@SuppressWarnings("unchecked")
		Set<String> urls =  (Set<String>) session.getAttribute("urls");
		String contextPath = request.getContextPath();
		String requestUri = request.getRequestURI();
		
		if(!contextPath.isEmpty()) {
			// 如果有contextPath需要截取掉，因为数据库里面记录的URL都没有ContextPath
			requestUri = requestUri.substring(contextPath.length());
		}
		
		for(String url : urls) {
			if(url.equals(requestUri)) {
				return true;
			}
			
			// url里面是包含正则表达式
			if(requestUri.matches(url)) {
				return true;
			}
			
			if(requestUri.contains(url)) {
				return true;
			}
			
		}
		LOG.trace("访问被拒绝，访问URL：{}，用户的URL集合：{}", requestUri, urls);
		return false;
	}
}
