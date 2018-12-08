package org.fkjava.hr.controller;

import java.util.List;

import org.fkjava.hr.domain.Department;
import org.fkjava.hr.service.HumanResourcesService;
import org.fkjava.identity.domain.User;
import org.fkjava.identity.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/human-resources/department")
public class DepartmentController {

	@Autowired
	private IdentityService identityService;
	@Autowired
	private HumanResourcesService humanResourcesService;
	@GetMapping
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("human-resources/department/index");
		// 模拟查询，后面要改为AJAX
		Page<User> userPage = this.identityService.findUsers(null,0);
		List<User> users = userPage.getContent();
		mav.addObject("users", users);
		return mav;
	}
	
	@GetMapping(produces="application/json")
	@ResponseBody
	public  List<Department> findTopDepartments(){
		return this.humanResourcesService.findTopDepartments();
	}
	
	@PostMapping
	public String save(Department department) {
		this.humanResourcesService.save(department);
		return "redirect:/human-resources/department";
	}
}
