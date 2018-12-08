package org.fkjava.hr.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="hr_department")
public class Department implements Serializable{

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
	
	private Double number;
	@ManyToOne
	@JoinColumn(name="parent_id")
	private Department parent;
	
	@OneToMany(mappedBy="parent")
	@JsonProperty("children")
	@OrderBy("number")
	private List<Department> childs;
	
	// 经理，必须是员工；员工必须是用户。
	@OneToOne
	@JoinColumn(name="manger_id")
	private Employee manger;
	
	// 部门里面的员工，关系由Employee来维护
	@OneToMany(mappedBy="department")
	@JsonIgnore
	private List<Employee> employees;

	public String getId() {
		return id;
	}
	
	public Double getNumber() {
		return number;
	}

	public void setNumber(Double number) {
		this.number = number;
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

	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public List<Department> getChilds() {
		return childs;
	}

	public void setChilds(List<Department> childs) {
		this.childs = childs;
	}

	public Employee getManger() {
		return manger;
	}

	public void setManger(Employee manger) {
		this.manger = manger;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
	
	
}
