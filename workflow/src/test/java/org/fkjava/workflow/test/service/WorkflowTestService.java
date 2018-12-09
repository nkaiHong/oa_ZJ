package org.fkjava.workflow.test.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.fkjava.common.data.domain.Result;
import org.fkjava.identity.UserHolder;
import org.fkjava.identity.domain.Role;
import org.fkjava.identity.domain.User;
import org.fkjava.workflow.WorkflowConfig;
import org.fkjava.workflow.service.WorkflowService;
import org.fkjava.workflow.vo.ProcessForm;
import org.fkjava.workflow.vo.TaskForm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WorkflowConfig.class})
//AbstractJUnit4SpringContextTests 没有事务，测试的数据直接同步到数据库
//AbstractTransactionalJUnit4SpringContextTests 有事务，会在测试方法完成以后回滚事务 ，避免污染测试数据库
public class WorkflowTestService 
extends AbstractJUnit4SpringContextTests{
//extends AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private WorkflowService workflowService;
	private String processDefinitionId;
	
	@Before
	public void testDeploySuccess() throws IOException, URISyntaxException {
		String name="helloworld";
		
		
		// 把测试文件压缩起来，方便测试
		// 可以直接导出文件，生成zip文件，也可以直接在内存里面压缩
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try(ZipOutputStream out = new ZipOutputStream(outputStream)){
			this.addFile(out,name + ".bpmn");
			this.addFile(out,name + ".png");
			this.addFile(out, name + "-task1.html");
			
		} 
		
		try(InputStream in = new ByteArrayInputStream(outputStream.toByteArray())){
			Result result = this.workflowService.deploy(name,in);
			Assert.assertEquals(Result.CODE_OK, result.getCode());
		}
		
		
		ProcessDefinition definition = this.workflowService.findDefinitionByKey(name);
		if(!StringUtils.isEmpty(definition)) {
		processDefinitionId = definition.getId();
		}
		
		// 模拟用户、用户的角色（用户组）
		// 需要把identity模块加入进来
		User user = new User();
		user.setId("用户1");
		user.setName("用户1");
		
		Role role = new Role();
		role.setId("用户组1");
		role.setName("用户组1");
		
		List<Role> roles = new LinkedList<>();
		roles.add(role);
		user.setRoles(roles);
		
		UserHolder.set(user);
		
		// 告诉流程引擎：当前用户的ID是什么！
		// 这句话必须要放入拦截器里面
		Authentication.setAuthenticatedUserId("初始测试用户");
	}

	private void addFile(ZipOutputStream out, String name) throws IOException, URISyntaxException {
		// 写入一个文件的标志
		ZipEntry bqmn = new ZipEntry(name);
		out.putNextEntry(bqmn);
		//写入内容
		URL bqmnUrl = this.getClass().getResource("/diagrams/" + name);
		File bqmnFile = new File(bqmnUrl.toURI());
		Files.copy(bqmnFile.toPath(), out);
		System.out.println("----------------" + bqmnFile.toPath());
		System.out.println("---------------------" + bqmnUrl.toURI());
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
	
	@Test
	public void start() {
		String key = "helloworld";
		// 把表单显示在页面，然后页面填写完成以后，提交到控制器。有控制器启动调用业务逻辑启动实例
		// 一个流程定义，有很多的流程实例。
		ProcessForm form = this.workflowService.findDifitionByKey(key);
		Assert.assertNotNull(form);
		
		// 启动流程实例
		// 用户填写的数据，没有流程都不同，所以此时只能获取所有的请求参数，交给业务逻辑层的统一代码去处理
		// request.getParameterMap()返回一个Map，现在这里模拟一个。
		Map<String, String[]> params = new HashMap<>();
		// 根据流程定义的ID来启动流程实例
		String processDefinitionId = form.getDefinition().getId();
		System.out.println("开始启动流程引擎实例");
		Result result = this.workflowService.start(processDefinitionId,params);
		System.out.println("流程引擎实例启动结束");
		Assert.assertNotNull(result);
		System.out.println(result.getCode());
		Assert.assertEquals(Result.CODE_OK, result.getCode());
	}
	
	//查询待办信息
	@Test
	public void findTasks() {
		//启动一个流程引擎实例用于测试待办信息
		start();
		System.out.println("查询待办任务");
		String keyword = null;
		int pageNumber = 0;
		
		Page<TaskForm> page = this.workflowService.findTasks(keyword,null,pageNumber);
		Assert.assertNotNull(page);
		Assert.assertTrue("必须要有数据", page.getTotalElements() > 0);
		
	}
	
	//打开待办
	@Test
	public void findTask() {
		// 启动一个流程实例用于测试待办任务
		start();
		
		String keyword = null;
		int pageNumber = 0;
		Page<TaskForm> page = this.workflowService.findTasks(keyword, null, pageNumber);
		Assert.assertNotNull(page);
		Assert.assertTrue("必须要有数据",page.getTotalElements() > 0);
		
		// 获取第一个任务的id，然后根据id查询待办任务的详情，包括：表单内容、表单属性、表单名称
		String taskId = page.getContent().get(0).getTask().getId();
		TaskForm taskForm = this.workflowService.getTaskForm(taskId);

		Assert.assertNotNull(taskForm);
		System.out.println("------------" + taskForm);
		Assert.assertNotNull(taskForm.getContent());//表单的内容不为空
		Assert.assertNotNull(taskForm.getFormKey());//表单的名字不为空
		Assert.assertNotNull(taskForm.getFormData());//表单的属性也不为空

	}
	
	@Test
	public void testComplete() {
		// 启动一个流程实例用于测试待办任务
		start();
		
		String keyword = null;
		int pageNumber = 0;
		Page<TaskForm> page = this.workflowService.findTasks(keyword, null, pageNumber);
		Assert.assertNotNull(page);
		Assert.assertTrue("必须要有数据",page.getTotalElements() > 0);
		
		String taskId = page.getContent().get(0).getTask().getId();
		//完成任务以后，根据流程实例的id查询任务，检查新的任务是否为预期任务
		String processInstanceId = page.getContent().get(0).getTask().getProcessInstanceId();
		// 根据任务的ID完成任务。
		// 用户在页面上填写哪些信息，对于开发者来讲是不可预测的。
		// 所以为了让流程能够继续下一个步骤，必须要获取所有的请求参数。类似于启动流程实例的时候。
		Map<String, String[]> params = new HashMap<>();
		// 模拟页面传入的参数，用于【下一步】的判断
		params.put("参数", new String[] {"b"});
		
		this.workflowService.complete(taskId,params);
		
		// 测试以后，需要判断新的任务是否为【任务2】
		// 后面不是相同的用户的任务，所以清空当前用户
		UserHolder.remove();
		
		page = this.workflowService.findTasks(keyword, processInstanceId, pageNumber);
		Assert.assertNotNull(page);
		Assert.assertTrue("必须要有数据",page.getTotalElements() > 0);
		
		Task task  = page.getContent().get(0).getTask();
		Assert.assertEquals("任务一", task.getName());
	}
}
