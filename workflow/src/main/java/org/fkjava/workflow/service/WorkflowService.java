package org.fkjava.workflow.service;

import java.io.InputStream;

import org.activiti.engine.repository.ProcessDefinition;
import org.fkjava.common.data.domain.Result;
import org.springframework.data.domain.Page;

public interface WorkflowService {

	Result deploy(String name, InputStream in);

	Page<ProcessDefinition> findDefinitions(String keyword, int pageNumber);

	ProcessDefinition findDefinitionByKey(String key);

	void disableDefinition(String processDefinitionId);

	ProcessDefinition findDefinitionById(String processDefinitionId);

	void activeDefinition(String processDefinitionId);

	
}
