package org.fkjava.hr.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.fkjava.identity.domain.User;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="hr_employee")
public class Employee implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="uuid2")
	@GenericGenerator(name="uuid2",strategy="uuid2")
	@Column(length=36)
	private String id;
	
	//员工关联的用户
	@OneToOne
	@JoinColumn(name="user_id")
	private User user;
	
	//员工的归属部门
	@ManyToOne
	//@JoinColumn(name="department_id")
	// 遇到循环依赖的时候，一定要在某一段使用中间表来关联，否则会出现数据无法迁移的问题！
	// 部门需要部门经理，是一个员工。
	// 员工在部门里面，需要一个部门。
	@JoinTable(name="hr_employee_department",
	joinColumns= {@JoinColumn(name="employee_id")},
	inverseJoinColumns= {@JoinColumn(name="department_id")})
	@JsonIgnore
	private Department department;
	
	//入职时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date joinTime;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	public static enum Status{
		NORMAL;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Date getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	
}
