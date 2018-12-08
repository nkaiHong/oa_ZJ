package org.fkjava.menu.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.fkjava.identity.domain.Role;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="menu")
public class Menu implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator="uuid2")
	@GenericGenerator(name="uuid2",strategy="uuid2")
	@Column(length=36)
	private String id;
	private String name;
	private String url;
	
	@Enumerated(EnumType.STRING) //枚举类转化为String类型 默认就是int
	private Type type;
	
	//角色跟菜单的关系是多对多的,需要中间表来维护
	@ManyToMany
	@JoinTable(name="menu_roles")
	@OrderBy("name")//使用角色的名称进行排序
	private List<Role> roles;
	
	//排序的序号
	private Double number;
	
	//上级的菜单(父菜单)只有一个
	@ManyToOne()
	@JoinColumn(name="parent_id") // 上级菜单的id
	@JsonIgnore //生成json的时候，不要把parent也生成出去，否则会出现无限递归的情况。有坑
	private Menu parent;
	
	//下级的菜单下级菜单有多个
	@OneToMany(mappedBy="parent")
	@OrderBy("number")//查询下级菜单的时候，使用number进行排序
	@JsonProperty("children")//生成json的时候，使用一个别名  加上后下级菜单就出现了。！！！ 有坑
	private List<Menu> childs;
	
	public static enum Type{
		//链接型的
		LINK,
		//按钮
		BUTTON;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Double getNumber() {
		return number;
	}

	public void setNumber(Double number) {
		this.number = number;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public List<Menu> getChilds() {
		return childs;
	}

	public void setChilds(List<Menu> childs) {
		this.childs = childs;
	}
	
	
} 
