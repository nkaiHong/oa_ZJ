package org.fkjava.workflow.vo;

import org.activiti.engine.form.FormData;
import org.activiti.engine.repository.ProcessDefinition;

//VO : 值对象，专门用于封装多个值
public class ProcessForm {

	//流程定义
	private ProcessDefinition definition;
	
	//表单内容
	private Object content;
	
	//表单的名称
	private String formKey;
	
	//表单的数据，包括表单属性在里面
	private FormData formData;

	public ProcessDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(ProcessDefinition definition) {
		this.definition = definition;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public FormData getFormData() {
		return formData;
	}

	public void setFormData(FormData formData) {
		this.formData = formData;
	}
}
