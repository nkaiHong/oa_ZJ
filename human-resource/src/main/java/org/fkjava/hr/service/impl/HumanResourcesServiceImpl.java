package org.fkjava.hr.service.impl;

import java.util.List;

import org.fkjava.hr.domain.Department;
import org.fkjava.hr.domain.Employee;
import org.fkjava.hr.repository.DepartmentRepository;
import org.fkjava.hr.repository.EmployeeRepository;
import org.fkjava.hr.service.HumanResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HumanResourcesServiceImpl implements HumanResourcesService{

	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Override
	public void save(Department department) {
		if(department.getParent() != null && StringUtils.isEmpty(department.getParent().getId())) {
			department.setParent(null);
		}
		
		if(StringUtils.isEmpty(department.getId())) {
			department.setId(null);
		}
		
		//同级的部门，名字不能重复
		Department old;
		if(department.getParent() != null) {
			//表示有上级菜单的
			old = this.departmentRepository.findByParentAndName(department.getParent(),department.getName());
		}else {
			//表示没有上一级菜单的
			old = this.departmentRepository.findByNameAndParentNull(department.getName());
		}
		
		if(old != null && !old.getId().equals(department.getId())) {
			//查到了数据，但是id又不一样，表示重复了
			throw new IllegalArgumentException("部门的名称不能重复");
		}
		
		//2.设置部门经理为当前部门的员工
		//部门经理不为空，
		if(department.getManger() != null 
				&& department.getManger().getUser() != null
				&& !StringUtils.isEmpty(department.getManger().getUser().getId())) {
			
			Employee employee = department.getManger();
			Employee oldEmployee = this.employeeRepository.findByUser(employee.getUser());
			
			if(oldEmployee == null) {
				//没有User对应的员工，表示表用户还不是员工
				//此时就应该设置一下，把用户设置为员工，
				employee = this.employeeRepository.save(employee);
			}else {
				//就表示为原来的员工，在数据库可以查到
				employee = oldEmployee;
			}
			
			employee.setDepartment(department);
			department.setManger(employee);
		}else {
			// 没有选择部门经理
			// 在后面的其他业务处理的过程，如果一定需要部门经理处理，那么就要求使用独立的一个表来记录【委托】，把部门经理的职责委托给其他的用户
			// 委托的用户，不一定是当前部门的
			// 现在这里不处理委托
			department.setManger(null);
		}
		
		//3.排序的序号查询，处理
		if(old != null) {
			department.setNumber(old.getNumber());
		}else{
			Double maxNumber;
			if(department.getParent() == null ) {
				maxNumber = this.departmentRepository.findMaxNubmerByParentNull();
			}else {
				maxNumber = this.departmentRepository.findMaxNumberByParent(department.getParent());
			}
			
			if(maxNumber == null) {
				maxNumber = 0.0;
			}
			
			Double number = maxNumber + 10000000.0;
			department.setNumber(number);
		}
		
		this.departmentRepository.save(department);
		
	}
	@Override
	public List<Department> findTopDepartments() {
	
		return this.departmentRepository.findByParentNullOrderByNumber();
	}

}
