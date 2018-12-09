package org.fkjava.workflow.controller;

import java.util.Map;

import org.fkjava.common.data.domain.Result;
import org.fkjava.workflow.service.WorkflowService;
import org.fkjava.workflow.vo.ProcessForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/workflow/instance")
public class InstanceController {

	@Autowired
	private WorkflowService workflowService;
	
	@GetMapping("{key}")
	public ModelAndView start(@PathVariable("key")String key) {
		ModelAndView mav = new ModelAndView("workflow/instance/start");
		ProcessForm form = this.workflowService.findDifitionByKey(key);
		mav.addObject("form",form);
		return mav;
	}
	
	@PostMapping("{id}")
	public ModelAndView start(@PathVariable("id") String processDefinitionId,
			@RequestParam("processDefinitionKey") String processDefinitionKey,WebRequest request) {
		Map<String, String[]> params = request.getParameterMap();
		Result result = this.workflowService.start(processDefinitionId, params);
		if(result.getCode() == Result.CODE_OK) {
			ModelAndView mav = new ModelAndView("redirect:/workflow/history/instance");
			return mav;
		}else {
			ModelAndView mav = this.start(processDefinitionKey);
			mav.addObject("result", result);
			return mav;
		}
	}
}
