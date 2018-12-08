package org.fkjava.security.domain;

import java.util.Collection;

import org.fkjava.identity.domain.User;
import org.springframework.security.core.GrantedAuthority;

public class UserDetails extends org.springframework.security.core.userdetails.User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//用户在数据的id
	private String userId;
	/**
	 * 数据库里面的用户的姓名
	 */
	private String name;
	//构造器
	/**
	 * 
	 * @param username              登录名
	 * @param password              数据库里面加密后的密码
	 * @param enabled               是否激活
	 * @param accountNonExpired     账户是否未过期
	 * @param credentialsNonExpired 密码是否未过期
	 * @param accountNonLocked      账户是否未锁定
	 * @param authorities           集合，用户具有的角色、身份。我们在角色的时候有KEY，通常在KEY前面加上ROLE_即可。
	 */
	public UserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}
	
	//加多一个构造器
	/**
	 * 
	 * @param user        数据库里面存储的User对象
	 * @param authorities 集合，用户具有的角色、身份。我们在角色的时候有KEY，通常在KEY前面加上ROLE_即可。
	 */
	public UserDetails(User user,Collection<? extends GrantedAuthority> authorities) {
		super(user.getLoginName(), user.getPassword(), 
				user.getStatus() == User.Status.NORMAL,  //正常
				user.getStatus() != User.Status.EXPIRED, //不过期
				user.getStatus() != User.Status.EXPIRED,
				user.getStatus() != User.Status.DISABLE,//不禁用
				 authorities);
		this.userId = user.getId();
		this.name = user.getName();
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
