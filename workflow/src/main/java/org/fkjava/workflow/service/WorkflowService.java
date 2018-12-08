package org.fkjava.workflow.service;

import java.io.InputStream;
import java.util.Map;

import org.activiti.engine.repository.ProcessDefinition;
import org.fkjava.common.data.domain.Result;
import org.fkjava.workflow.vo.ProcessForm;
import org.springframework.data.domain.Page;

public interface WorkflowService {

	Result deploy(String name, InputStream in);

	Page<ProcessDefinition> findDefinitions(String keyword, int pageNumber);

	ProcessDefinition findDefinitionByKey(String key);

	void disableDefinition(String processDefinitionId);

	ProcessDefinition findDefinitionById(String processDefinitionId);

	void activeDefinition(String processDefinitionId);

	ProcessForm findDifitionByKey(String key);

	Result start(String processDefinitionId, Map<String, String[]> params);

	

	
}
