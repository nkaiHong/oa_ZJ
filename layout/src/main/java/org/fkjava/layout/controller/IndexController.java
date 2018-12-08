package org.fkjava.layout.controller;

import org.fkjava.identity.UserHolder;
import org.fkjava.identity.domain.User;
import org.fkjava.identity.service.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/layout")
public class IndexController {

	@Autowired
	private IdentityService identityService;
	
	@RequestMapping
	public String index() {
		return "layout/index";
	}
	
	@GetMapping("/person")
	public ModelAndView person(
			@RequestParam(name="pageNumber",defaultValue="0")Integer number,//页码
			@RequestParam(name="keyword",required=false)String keyword  ) {
		ModelAndView mav = new ModelAndView("layout/person");
		
		//查询第一页的数据
		Page<User> page = identityService.findUsers(keyword,number);
		//传到页面上去，Page是由Spring-boot封装好的一个类，里面有一切分页参数，并把参数都设置在list集合里面，
		//也就是content中，直接拿出来用就可以了，不用直接写代码
		mav.addObject("page", page);
		
		User user = UserHolder.get();
		mav.addObject("user", user);
		return mav;
	}
	
	@RequestMapping("ex")
	public String ex() {
		return	"layout/ex";
	}
}
