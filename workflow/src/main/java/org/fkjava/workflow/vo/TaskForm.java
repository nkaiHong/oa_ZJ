package org.fkjava.workflow.vo;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.fkjava.identity.domain.User;

//列表：流程的名称，流程的开始时间，流程的创始人，任务名称，任务创建的时间，任务过期时间
//表单的内容，表单名称，表单数据则显示任务的详情时使用
public class TaskForm extends ProcessForm{

	// 流程实例，历史流程实例里面的信息比较完整
	private HistoricProcessInstance instance;
	// 流程的创始人（谁创建流程、谁提交流程）
	private User initialUser;
	// 任务的运行时实例
	private Task task;
	public HistoricProcessInstance getInstance() {
		return instance;
	}
	public void setInstance(HistoricProcessInstance instance) {
		this.instance = instance;
	}
	public User getInitialUser() {
		return initialUser;
	}
	public void setInitialUser(User initialUser) {
		this.initialUser = initialUser;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	
	
}
