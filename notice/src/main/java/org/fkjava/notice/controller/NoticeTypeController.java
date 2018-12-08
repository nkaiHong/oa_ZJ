package org.fkjava.notice.controller;

import java.util.List;

import org.fkjava.notice.domain.NoticeType;
import org.fkjava.notice.service.NoticeService;
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
@RequestMapping("/notice/type")
public class NoticeTypeController {

	@Autowired
	private NoticeService noticeService;
	
	@GetMapping
	public ModelAndView index() {
		
		ModelAndView mav = new ModelAndView("notice/type/index");
		
		List<NoticeType> types = this.noticeService.findAllTypes();
		mav.addObject("types", types);
		return mav;
	}
	
	@PostMapping
	public String save(NoticeType type) {
		noticeService.save(type);
		return "redirect:/notice/type";
	}
	
	@DeleteMapping("{id}")
	@ResponseBody
	public String delete(@PathVariable("id")String id) {
		this.noticeService.deleteTypeById(id);
		return "Ok";
	}
	
}
