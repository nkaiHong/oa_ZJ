package org.fkjava.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fkjava.identity.UserHolder;
import org.fkjava.identity.domain.User;
import org.fkjava.security.domain.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserHolderInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(SecurityContextHolder.getContext().getAuthentication() == null) {
			return true;
		}
		
		//获取Spring Security里面保存的UserDetails对象，并且转换为User存储在UserHolder里面
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//Spring Security在没有登陆的时候，会把当前用户设置为【匿名用户】
		//anonymous  ---》表示匿名的
		
		//解决bug,在没有用户登陆的时候，是匿名的用户，不加判断会出错
		if(principal instanceof UserDetails){
			UserDetails userDetails = (UserDetails)principal;
			
			User user = new User();
			user.setId(userDetails.getUserId());
			user.setName(userDetails.getName());
			//把User设置到工具类的线程里面
			UserHolder.set(user);
		}
		
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//设置完之后要清理现场
		UserHolder.remove();
	}
}
