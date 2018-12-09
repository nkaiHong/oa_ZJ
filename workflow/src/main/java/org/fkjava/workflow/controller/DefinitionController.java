package org.fkjava.workflow.controller;

import java.io.IOException;
import java.io.InputStream;

import org.activiti.engine.repository.ProcessDefinition;
import org.fkjava.workflow.service.WorkflowService;
import org.fkjava.workflow.vo.ProcessForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/workflow/definition")
public class DefinitionController {

	@Autowired
	private WorkflowService workflowService;
	@GetMapping
	public ModelAndView index(
			@RequestParam(name="keyword",required = false)String keyword,
			@RequestParam(name="pageNumber",defaultValue = "0")int pageNumber) {
		Page<ProcessDefinition> page = this.workflowService.findDefinitions(keyword,pageNumber);
		ModelAndView mav = new ModelAndView("workflow/definition/index");
		mav.addObject("page",page);
		return mav;
	}
	
	@GetMapping("disable/{id}")
	public String disable(@PathVariable("id") String id) {
		this.workflowService.disableDefinition(id);
		return "redirect:/workflow/definition";
	}

	@GetMapping("active/{id}")
	public String active(@PathVariable("id") String id) {
		this.workflowService.activeDefinition(id);
		return "redirect:/workflow/definition";
	}

	
	@PostMapping
	public String upload(@RequestParam("file")MultipartFile file) throws IOException {
		String name=file.getOriginalFilename();
		try(InputStream in = file.getInputStream()){
			this.workflowService.deploy(name, in);
		}
		return "redirect:/workflow/definition";
	}
}
