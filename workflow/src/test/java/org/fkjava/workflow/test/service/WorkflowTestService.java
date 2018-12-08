package org.fkjava.workflow.test.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.activiti.engine.repository.ProcessDefinition;
import org.fkjava.common.data.domain.Result;
import org.fkjava.workflow.WorkflowConfig;
import org.fkjava.workflow.service.WorkflowService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WorkflowConfig.class})
public class WorkflowTestService extends AbstractJUnit4SpringContextTests{

	@Autowired
	private WorkflowService workflowService;
	
	@Test
	public void testDeploySuccess() throws IOException, URISyntaxException {
		String name="helloworld";
		
		
		// 把测试文件压缩起来，方便测试
		// 可以直接导出文件，生成zip文件，也可以直接在内存里面压缩
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try(ZipOutputStream out = new ZipOutputStream(outputStream)){
			this.addFile(out,name + ".bpmn");
			this.addFile(out,name + ".png");
		}
		
		try(InputStream in = new ByteArrayInputStream(outputStream.toByteArray())){
			Result result = this.workflowService.deploy(name,in);
			Assert.assertEquals(Result.CODE_OK, result.getCode());
		}
	}

	private void addFile(ZipOutputStream out, String name) throws IOException, URISyntaxException {
		// 写入一个文件的标志
		ZipEntry bqmn = new ZipEntry(name);
		out.putNextEntry(bqmn);
		//写入内容
		URL bqmnUrl = this.getClass().getResource("/diagrams/" + name);
		File bqmnFile = new File(bqmnUrl.toURI());
		Files.copy(bqmnFile.toPath(), out);
	}
	
	@Test
	public void testFindDefinitions() {
		//测试流程定义查询
		String keyword = null;
		int pageNumber = 0;//第一页
		Page<ProcessDefinition> page = this.workflowService.findDefinitions(keyword,pageNumber);
		Assert.assertNotNull("必须要求放回一行数据",page);
		Assert.assertEquals("预期要有数据，总记录数要大于0", true,page.getTotalElements() > 0);
	}
	
	String processDefinitionId;
	
	@Before
	public void findDefinitionByKey() {
		String key="helloworld";
		ProcessDefinition definition = this.workflowService.findDefinitionByKey(key);
		processDefinitionId = definition.getId();
	}
	
	@Test
	public void disable() {
		// 禁用流程定义，要求传入一个ID
		// 直接查询一个流程定义来进行模拟，写入@Before方法里面
		// String processDefinitionId = "";

		// 执行禁用
		this.workflowService.disableDefinition(processDefinitionId);
		
		// 查询流程定义，检查是否禁用成功
		ProcessDefinition definition = this.workflowService.findDefinitionById(processDefinitionId);
		Assert.assertNotNull(definition);
		Assert.assertEquals(true, definition.isSuspended());
		
		//执行激活
		this.workflowService.activeDefinition(processDefinitionId);
		// 查询流程定义，检查是否激活成功
		definition = this.workflowService.findDefinitionById(processDefinitionId);
		
		Assert.assertNotNull(definition);
		//isSuspended:流程实例是否停止
		Assert.assertEquals(false, definition.isSuspended());
	}
}
