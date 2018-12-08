package org.fkjava.workflow.test;

import org.activiti.engine.RepositoryService;
import org.fkjava.workflow.WorkflowConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WorkflowConfig.class})
public class WorkflowTest extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private RepositoryService repositoryService;
	
	@Test
	public void test() {
		System.out.println("测试初始化流程引擎");
	}
}
