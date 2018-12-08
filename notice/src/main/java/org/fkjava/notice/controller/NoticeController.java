package org.fkjava.notice.controller;

import java.util.List;

import org.fkjava.common.data.domain.Result;
import org.fkjava.notice.domain.Notice;
import org.fkjava.notice.domain.NoticeRead;
import org.fkjava.notice.domain.NoticeType;
import org.fkjava.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/notice")
public class NoticeController {

	@Autowired
	private NoticeService noticeService;
	@GetMapping
	public ModelAndView index(
			@RequestParam(name="pageNumber",defaultValue="0") Integer number,
			@RequestParam(name="keyword",required = false)String keyword) {
		ModelAndView mav = new ModelAndView("notice/index");
		
		Page<NoticeRead> page = this.noticeService.findNotices(number,keyword);
		mav.addObject("page", page);
		
		return mav;
	}
	
	@GetMapping("add")
	public ModelAndView add() {
		ModelAndView mav = new ModelAndView("notice/add");
		List<NoticeType> types = this.noticeService.findAllTypes();
		mav.addObject("types",types);
		return mav;
	}

	@PostMapping
	public ModelAndView save(Notice notice) {
		ModelAndView mav = new ModelAndView("redirect:/notice");
		this.noticeService.write(notice);
		return mav;
	}
	
	@GetMapping("{id}")
	public ModelAndView read(@PathVariable("id")String id) {
		ModelAndView mav = new ModelAndView("notice/read");
		
		Notice notice = this.noticeService.findById(id);
		//System.out.println(notice);
		mav.addObject("notice", notice);
		return mav;
	}
	
	
	@PostMapping("{id}")
	@ResponseBody
	public Result readed(@PathVariable("id")String id) {
		this.noticeService.read(id);
		return Result.ok();
	}
	
	//撤回
	@GetMapping("recall/{id}")
	public String recall(@PathVariable("id")String id) {
		this.noticeService.recall(id);
		return "redirect:/notice";
	}
	
	//删除
	@DeleteMapping("{id}")
	@ResponseBody
	public Result delete(@PathVariable("id")String id) {
		this.noticeService.deletebyId(id);
		return Result.ok();
	}
	
	@GetMapping("edit/{id}")
	public ModelAndView edit(@PathVariable("id")String id) {
		
		ModelAndView mav = new ModelAndView("notice/add");
		Notice notice = this.noticeService.findById(id);
		mav.addObject("notice", notice);
		
		List<NoticeType> types = noticeService.findAllTypes();
		mav.addObject("types",types);
		return mav;
	}
	
	@GetMapping("publish/{id}")
	public String publish(@PathVariable("id")String id) {
		this.noticeService.publish(id);
		return "redirect:/notice";
	}
}
