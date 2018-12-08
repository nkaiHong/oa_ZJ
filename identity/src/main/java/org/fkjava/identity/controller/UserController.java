package org.fkjava.identity.controller;

import java.util.LinkedList;
import java.util.List;

import org.fkjava.identity.UserHolder;
import org.fkjava.identity.domain.Role;
import org.fkjava.identity.domain.User;
import org.fkjava.identity.service.IdentityService;
import org.fkjava.identity.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/identity/user")
//因为浏览器是不安全的，通过F12可以查到用户的id，并且可以恶意的修改，会给程序的判断流程造成很大影响
//所以要把修稿的用户的ID存储在Session里面，也就是放在服务器内部，避免被恶意修改，
//即便在浏览器上作修改也不会造成影响，因为拿到的是session里面的id而不是在浏览器上的id
@SessionAttributes({"modifyUserId"})
public class UserController {
	
	@Autowired
	private IdentityService identityService; 
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping
	public ModelAndView index(
			// Model是通常放到方法参数列表中的，用于控制器和JSP传值，也可以作为方法返回值
			// View只是用来返回页面，可以作为返回值
			// ModelAndView是方法返回值，包含了数据和视图
			//number是后面分页用到，keyword则是作为关键词搜索
			@RequestParam(name="pageNumber",defaultValue="0")Integer number,//页码
			@RequestParam(name="keyword",required=false)String keyword)   //关键字查询
	{
		ModelAndView mav = new ModelAndView("identity/user/index");
		
		//查询第一页的数据
		Page<User> page = identityService.findUsers(keyword,number);
		//传到页面上去，Page是由Spring-boot封装好的一个类，里面有一切分页参数，并把参数都设置在list集合里面，
		//也就是content中，直接拿出来用就可以了，不用直接写代码
		mav.addObject("page", page);
		return mav;
	}
	
	@GetMapping(produces="application/json")
	@ResponseBody
	public AutoCompleteResponse likeName(
			 // 搜索的关键字
			@RequestParam(name="query")String keyword) {
		List<User> users = identityService.findUsers(keyword);
		
		List<User> result = new LinkedList<>(); 
		users.forEach(user ->{
			User u = new User();
			u.setId(user.getId());
			u.setName(user.getName());
			result.add(u);
		});
		
		return new AutoCompleteResponse(result);
	}

	/**
	 * 自动完成的响应对象
	 * 
	 * @author lwq
	 *
	 */
	
	public static class AutoCompleteResponse {
		private List<AutoCompleteItem> suggestions;

		public AutoCompleteResponse(List<User> users) {
			super();
			this.suggestions = new LinkedList<>();
			users.forEach(u -> {
				AutoCompleteItem item = new AutoCompleteItem(u);
				this.suggestions.add(item);
			});
		}

		public List<AutoCompleteItem> getSuggestions() {
			return suggestions;
		}
	}
	
	public static class AutoCompleteItem{
		private User user;
		private String value;
		
		public AutoCompleteItem(User user) {
			super();
			this.user = user;
			this.value = user.getName();
		}

		public User getUser() {
			return user;
		}

		public String getValue() {
			return value;
		}

		
		
	}
	
	@GetMapping("/add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("identity/user/add");
		//后面这里要查询数据，因为每一个用户都有【身份】、【角色】 ，在修改用户信息的时候，需要选择用户的身份
		//把所以不是属于普通管理员的全部查出来,也就是不固定的那些。
		List<Role> roles = this.roleService.findAllNotFixed();
		mav.addObject("roles", roles);
		return mav;
	}
	
	@PostMapping
	public String save(User user,
			// 从Session里面获取要修改的用户的ID
			@SessionAttribute(value="modifyUserId",required = false)String modifyUserId,
			//这个是移除掉session里面的用户的id
			SessionStatus sessionStatus) {
		
		//修改用户的时候，把用户的ID设置到User对象里面
		//user对象没有id的时候表示新增，新增的时候不需要id。因为id是自动生成的
		//服务器session里面的id --》modifyUserId != null
		//!StringUtils.isEmpty(user.getId()就是说用户那边传过来的id不等于空 满足这两个条件才能修改，否则就是新增
		//!StringUtils.isEmpty(user.getId())这个条件的判断就是当我在修改的时候突然想要新增，然后到达新增的页面的，
		//但是我的session里面还是有保存原来用户的id，所有新增变成修改这个问题。
		if(modifyUserId != null && !StringUtils.isEmpty(user.getId())) {
			user.setId(modifyUserId);
		}
		identityService.save(user);
		
		//清理现场，Session里面的modifyUserId 
		sessionStatus.setComplete();
		return "redirect:/identity/user";
	}
	
	@GetMapping("/{id}")
	public ModelAndView detail(@PathVariable("id")String id) {
		//页面跟添加一样，只是需要把User根据id查询出来
			ModelAndView mav = this.add();
			User user = this.identityService.findUserById(id);
		mav.addObject("user",user);
		//要把修稿的用户的ID存储在Session里面，避免浏览器被恶意修改！
		mav.addObject("modifyUserId", user.getId());
		return mav;
	}
	
	//激活身份的action 根据id来激活
	@GetMapping("/active/{id}")
	public String active(@PathVariable("id")String id) {
		this.identityService.active(id);
		return "redirect:/identity/user";
	}
	
	//禁用身份的action 根据id来禁用
	@GetMapping("/disable/{id}")
	public String disable(@PathVariable("id")String id) {
		this.identityService.disable(id);
		return "redirect:/identity/user";
	}
	
}
