package org.fkjava.identity.domain;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="id_role")
@Cacheable //因为查询用户的每次都会查询角色，直接把角色放在缓存里面，下次直接在缓存里面获取。
// 可以被缓存的，提高查询效率
public class Role implements Serializable{

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
	@Column(unique=true) //唯一键
	private String roleKey;
	
	/**
	 * 是否有固定的角色
	 * @return
	 */
	//没有固定的角色
	private boolean fixed = false;
	
	//hashCod算发，在页面为用户添加角色的时候，如果同时添加2个，
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleKey == null) ? 0 : roleKey.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (roleKey == null) {
			if (other.roleKey != null)
				return false;
		} else if (!roleKey.equals(other.roleKey))
			return false;
		return true;
	}
	
	
	public boolean isFixed() {
		return fixed;
	}
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
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
	public String getRoleKey() {
		return roleKey;
	}
	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}
	
	
}
