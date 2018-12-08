package org.fkjava.identity;

import org.fkjava.identity.domain.User;

/**
 * 
 * 此工具是把 User对象存储在当前线程里面，方便在其他的模块里面被获取
 * @author Administrator
 *
 */
public class UserHolder {
	//这个模块里面没有依赖security模块，所以线程里面只能放User对象
	private static final ThreadLocal<User> THREAD_LOCAL = new ThreadLocal<>();
	
	//获得当前线程的User对象
	public static User get() {
		return THREAD_LOCAL.get();
	}
	
	//设置User对象
	public static void set(User user) {
		THREAD_LOCAL.set(user);
	}
	
	//移除当前线程里面的User
	public static void remove() {
		THREAD_LOCAL.remove();
	}
}
