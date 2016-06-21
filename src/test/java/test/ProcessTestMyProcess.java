package test;




import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessTestMyProcess {

	// 部署流程定义
	@Test
	public void deploymentProcessDefinition() {
		// 创建核心引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService()// 与定义流程和部署对象相关的service
				.createDeployment()// 创建一个部署对象
				.name("helloworld入门程序")// 添加部署名称
				.addClasspathResource("MyProcess1.bpmn")// classpath的资源中加载，一次只能加载,
														// 一个文件
				.addClasspathResource("MyProcess1.png")// classpath的资源中加载，一次只能加载,
														// 一个文件
				.deploy();// 完成部署
		System.out.println("部署id:" + deployment.getId());
		System.out.println("部署名称：" + deployment.getName());
	}

	//
	@Test
	public void startProcessInstance() {
		// 创建核心引擎对象
				ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
				Deployment deployment = processEngine.getRepositoryService()// 与定义流程和部署对象相关的service
						.createDeployment()// 创建一个部署对象
						.name("helloworld入门程序")// 添加部署名称
						.addClasspathResource("MyProcess1.bpmn")// classpath的资源中加载，一次只能加载,
																// 一个文件
						.addClasspathResource("MyProcess1.png")// classpath的资源中加载，一次只能加载,
																// 一个文件
						.deploy();// 完成部署
				
		String processDefinitionKey = "myProcessTest1";
		ProcessInstance pi = processEngine.getRuntimeService()// 于正在执行的流程实例和执行对象相关的Service
				.startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例，key对应hellworld.bpmn文件中id的属性
		// 值，使用key值启动，默认是按照最新版本的流程定义启动
		System.out.println("流程实例ID:" + pi.getId());// 流程实例ID 101
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); // 流程定义ID
																		// HelloWorld:1:4
	}
	
	// 查询当前人的个人任务 
	@Test
	public void findMyPersonTask(){
		String assignee="张三";
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		List<Task> list=processEngine.getTaskService()// 与正在执行的认为管理相关的Service
				.createTaskQuery()//创建人物查询对象
				.taskAssignee(assignee)// 指定个人认为查询，指定办理人
				.list();
		if(list !=null && list.size()>0){
			for(Task task:list){
				   System.out.println("任务ID:"+task.getId());  
		            System.out.println("任务名称:"+task.getName());  
		            System.out.println("任务的创建时间"+task);  
		            System.out.println("任务的办理人:"+task.getAssignee());  
		            System.out.println("流程实例ID:"+task.getProcessInstanceId());  
		            System.out.println("执行对象ID:"+task.getExecutionId());  
		            System.out.println("流程定义ID:"+task.getProcessDefinitionId());  
		            System.out.println("#################################");  
				
			}
		}
	}
	
	//完成我的任务 
	@Test
	public void compeleMyPersonTask(){
		// 创建核心引擎对象
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		Deployment deployment = processEngine.getRepositoryService()// 与定义流程和部署对象相关的service
				.createDeployment()// 创建一个部署对象
				.name("helloworld入门程序")// 添加部署名称
				.addClasspathResource("MyProcess1.bpmn")// classpath的资源中加载，一次只能加载,
														// 一个文件
				.addClasspathResource("MyProcess1.png")// classpath的资源中加载，一次只能加载,
														// 一个文件
				.deploy();// 完成部署
		
		String processDefinitionKey = "myProcessTest1";
		ProcessInstance pi = processEngine.getRuntimeService()// 于正在执行的流程实例和执行对象相关的Service
		.startProcessInstanceByKey(processDefinitionKey);
		//任务id
		//String taskId="5";
		processEngine.getTaskService().complete(processDefinitionKey);
		 System.out.println("完成任务:任务ID:"+pi.getId());  
		
	}
	

}