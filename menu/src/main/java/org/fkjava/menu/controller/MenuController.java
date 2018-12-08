package org.fkjava.menu.controller;

import java.util.List;

import org.fkjava.common.data.domain.Result;
import org.fkjava.identity.domain.Role;
import org.fkjava.identity.service.RoleService;
import org.fkjava.menu.domain.Menu;
import org.fkjava.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/menu")
@SessionAttributes("menusJson")
public class MenuController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	
	@GetMapping
	public ModelAndView index() {
		
		ModelAndView mav = new ModelAndView("menu/index");
		
		List<Role> roles = this.roleService.findAll();
		mav.addObject("roles",roles);
		return mav;
	}
	
	// 如果客户端要求返回JSON的时候，调用下面这个方法
	@GetMapping(produces="application/json")
	@ResponseBody
	public List<Menu> findTopMenus(){
		return this.menuService.findTopMenus();
	}
	
	@PostMapping
	public String save(Menu menu) {
		
		this.menuService.save(menu);
		return "redirect:/menu";
	}
	
	@PostMapping("move")
	@ResponseBody
	public Result move(String id,String targetId,String moveType) {
		//拖动，移动
		return this.menuService.move(id,targetId,moveType);
	}
	
	@DeleteMapping("{id}")
	@ResponseBody
	public Result delete(@PathVariable("id") String id) {
		return this.menuService.delete(id);
		
	} 
	
	// 如果客户端要求返回JSON的时候，调用下面这个方法
	@GetMapping(value="menus",produces="application/json")
	@ResponseBody
	/*public List<Menu> findMyMenus(){
		// 找当前用户的菜单
		//找一级菜单
		return this.menuService.findMyMenus();
	}*/
	public String findMenus(@ModelAttribute("menusJson")String menusJson) {
		return menusJson;
	}
}
