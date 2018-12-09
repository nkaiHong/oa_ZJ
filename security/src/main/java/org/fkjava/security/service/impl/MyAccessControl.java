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
		
		if (urls == null) {
			// 当用户没有url的时候，表示还未登录，直接允许访问
			return true;
		}
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
			
			/*if(requestUri.contains(url)) {
				return true;
			}*/
			// url判断是否以一个*结尾，如果是则进行比较和匹配路径参数
			// workflow/definition/HelloWorld:1:3333
			// workflow/definition/* 要可以匹配上
			// workflow/definition/** 不处理
			if (url.charAt(url.length() - 1) == '*' // 最后一个字符
					&& url.charAt(url.length() - 2) != '*'// 倒数第二个字符
			) {
				if (requestUri.indexOf(url.substring(0, url.length() - 1)) >= 0) {
					return true;
				}
			}
		}
		LOG.trace("访问被拒绝，访问URL：{}，用户的URL集合：{}", requestUri, urls);
		return false;
	}
}
