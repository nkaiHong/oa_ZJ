package org.fkjava.workflow.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.fkjava.common.data.domain.Result;
import org.fkjava.workflow.service.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class WorkflowServiceImpl implements WorkflowService{

	private Logger log = LoggerFactory.getLogger(WorkflowServiceImpl.class);
	//org.activiti.engine.RepositoryService  可作为dao层使用
	@Autowired
	private RepositoryService repositoryService;
	@Override
	public Result deploy(String name, InputStream in) {
		// 解压缩，里面会包含bpmn、png，甚至有一些其他的文件
		// 这些文件，全部打包成ZIP格式压缩文件，一并上传。
		try(ZipInputStream zipInputStream = new ZipInputStream(in)){
			// 创建部署构建器
			DeploymentBuilder builder = this.repositoryService.createDeployment();
			
			//部署的信息
			builder.name(name);
			builder.addZipInputStream(zipInputStream);
			
			//执行部署
			builder.deploy();
			return Result.ok("流程定义部署成功！");
		} catch (IOException e) {
			log.error("流程定义部署失败" + e.getLocalizedMessage(),e);
			return Result.error("流程定义部署失败！");
		}
	}
	
	@Override
	public Page<ProcessDefinition> findDefinitions(String keyword, int pageNumber) {
		//每页读取10条数据
		Pageable pageable = PageRequest.of(pageNumber, 10);
		
		// 虽然Page和Pageable是Spring Data里面，但是Activiti没有Spring Data的支持，需要自己查询数据
		// 1.创建流程定义查询对象
		ProcessDefinitionQuery query = this.repositoryService.createProcessDefinitionQuery();
		// 2.设置查询条件
		if(StringUtils.isEmpty(keyword)) {
			//每页关键字查询
		}else {
			//有关键字查询
			keyword = "%" + keyword + "%";
			query.processDefinitionNameLike(keyword);//设置关键字查询
		}
		//只查询最后一个流程的流程定义（每个key对应最后一个版本的流程定义）
		query.latestVersion();
		//3.设置排序条件
		query.orderByProcessDefinitionKey().asc();
		//4.查询总记录数
		Long totalRows = query.count();
		
		//查询一页的数据
		List<ProcessDefinition> content = query.listPage((int) pageable.getOffset(), pageable.getPageSize());
		//6。构建Page对象
		Page<ProcessDefinition> page = new PageImpl<>(content,pageable,totalRows);
		return page;
	}

	@Override
	public ProcessDefinition findDefinitionByKey(String key) {
		ProcessDefinitionQuery query = this.repositoryService.createProcessDefinitionQuery();
		query.latestVersion();
		query.processDefinitionKey(key);
		return query.singleResult();
	}

	@Override
	public void disableDefinition(String processDefinitionId) {
		this.repositoryService.suspendProcessDefinitionById(processDefinitionId);
	}

	@Override
	public ProcessDefinition findDefinitionById(String processDefinitionId) {
		return this.repositoryService.getProcessDefinition(processDefinitionId);
	}

	@Override
	public void activeDefinition(String processDefinitionId) {
		this.repositoryService.activateProcessDefinitionById(processDefinitionId);
		
	}
	
}
