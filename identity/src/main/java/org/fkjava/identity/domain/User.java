package org.fkjava.identity.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="oa_user")
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator="uuid2")
	@GenericGenerator(name="uuid2",strategy="uuid2")
	@Column(length=36)
	private String id;
	@Column(length=20)
	private String name;
	@Column(unique=true,length=20)
	private String loginName;
	@Column(length=255)
	private String password;
	
	//角色关联用户
	//用户跟角色是多对多的关系
	
	@ManyToMany(fetch=FetchType.EAGER)//级联操作  查询用户的时候，把角色也查询出来
	//多对多需要中间表来维护
	@JoinTable(name="oa_user_roles",
	// user_id在id_user_roles表里面，指向、引用id_user表的id列
	joinColumns = {@JoinColumn(name="user_id")},//表示user对象的外键
	inverseJoinColumns = {@JoinColumn(name="role_id")})// 通过Role找到User的时候使用的key
	private List<Role> roles;
	
	//用于用户状态时间的统计
	//表示过期时间 在过期期间，每次修改，都把时间设置为2个月后，即是 加多两个月的时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiredTime; 
	
	
	// 把枚举的变量名称(STRING)存储到数据库 也就是状态的各个属性
	// 默认存储索引(ORDINAL)，如果不设置，则会按照索引排列
	@Enumerated(EnumType.STRING)
	private Status status;
	
	//用户的状态
	public static enum Status{
		//正常
		NORMAL,
		//过期
		EXPIRED,
		//禁用
		DISABLE
	}
	
	public Date getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}
	
	public Status getStatus() {
		if(this.status == Status.DISABLE) {
			return Status.DISABLE;
		}
		//如果过期时间为空，表示永远不会过期       当前时间 大于或等于过期的时间，表示已经过期
		if(this.expiredTime != null && System.currentTimeMillis() >= this.expiredTime.getTime()) {
			return Status.EXPIRED;
		}
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
