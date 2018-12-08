package org.fkjava.identity.controller;

import java.util.List;

import org.fkjava.identity.domain.Role;
import org.fkjava.identity.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/identity/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@GetMapping
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("identity/role/index");
		//角色管理,因为有多个角色，所有采用List集合
		List<Role> roles = roleService.findAllRoles();
		//把roles设置到session里面，可以在jsp页面用el表达式获取
		mav.addObject("roles", roles);
		return mav;
	}
	
	//点击保存的时候到这里，没有post方法必定会出现405
	@PostMapping
	public String save(Role role) {
		roleService.save(role);
		return "redirect:/identity/role";
	}
	
	@DeleteMapping("{id}") //因为删除的时候是根据id来删除，所有需要传入id,也是路径参数
	@ResponseBody //因为是放回json对象
	public String delete(@PathVariable("id") String id) {
		this.roleService.deleteById(id);
		return "OK";
	}
}
