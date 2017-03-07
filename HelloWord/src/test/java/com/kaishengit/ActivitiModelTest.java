/**
 * 
 */
package com.kaishengit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;



import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.h2.util.New;
import org.junit.Test;

import com.mysql.fabric.xmlrpc.base.Data;

import groovyjarjarasm.asm.tree.LocalVariableNode;

/** 
* @ClassName: ActivitiModelTest 
* @Description: 
* @author: 刘忠伟 
* @date: 2017年3月7日 上午9:20:28 
* @version 1.0
* 
*/

public class ActivitiModelTest {

	
	/**
	 * 初始化数据库，将所有表初始化到mysql///这个在项目中肯定是要写在代码块中，只配置一次
	 * 不需要xml文件
	 * @Description:
	 * @author: 刘忠伟
	 * @return:void
	 * @time:2017年3月7日 上午9:21:28
	 */
	@Test
	public void initMysql(){
		//创建一个引擎数据库配置
		ProcessEngineConfiguration configuration = 
				ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		//配置mysql
		configuration.setJdbcDriver("com.mysql.jdbc.Driver");
		configuration.setJdbcUrl("jdbc:mysql:///activiti_model");
		configuration.setJdbcUsername("root");
		configuration.setJdbcPassword("root");
		
		configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);//表不存在就自动创建表
		
		//创建引擎对象，创建完表才初始化创建完毕
		ProcessEngine engine  = configuration.buildProcessEngine();
		System.out.println(engine.getName());
	}
	
	/**
	 * mysql的表格的初始化，使用spring，需要xml文件不需要在代码中set注入。
	 * @Description:
	 * @author: 刘忠伟
	 * @return:void
	 * @time:2017年3月7日 上午10:24:01
	 */
	@Test
	public void initMysqlBySpring(){
		//spring注入数据库配置后，只需要创建引擎对象，即可初始化表格
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		System.out.println(engine.getName());
	}
	
	
	
	
	
	//Activiti中servic	API,七大Service接口。
	//所有service接口的调用都是通过引擎的，所以创建一个引擎
	private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	
	
	/**
	 * 第一步：部署流程，定义流程
	 * @Description:
	 * @author: 刘忠伟
	 * @return:void
	 * @time:2017年3月7日 上午11:17:37
	 */
	@Test
	public void deployment(){
		//定义流程，引擎调用与定义流程相关的service。
		Deployment deployment = engine.getRepositoryService()
				.createDeployment()//创建一个部署流程对象
				.name("hello!世界")//添加定义流程的名字（流程名）
				.addClasspathResource("diagrams/MyProcess1.bpmn")//从classpath中加载资源，一次只能加载一个
				.addClasspathResource("diagrams/Myprocess1.png")//两个流程定义包括图片都要加载
				.deploy();//部署
		
		System.out.println(deployment.getName());
		System.out.println(deployment.getId());
	}
	/**
	 * 已zip压缩包的形式部署定义流程。	创建zip包的zip输入流，将输入流部署即可。
	 * @Description:
	 * @author: 刘忠伟
	 * @return:void
	 * @time:2017年3月7日 下午4:51:50
	 */
	@Test
	public void zipDeployment(){
		//创建zip文件的输入流
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("diagrams/MyProcess2.zip");
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		
		//是定义流程所以使用Repositoryservice
		Deployment deployment = engine.getRepositoryService()
				.createDeployment()//创建一个部署对象
				.name("请假流程")//部署流程的名字
				.addZipInputStream(zipInputStream)
				.deploy();//完成部署
		
		System.out.println(deployment.getName());
	}
	
	
	/**
	 * 第二部：启动流程实例，使用RuntimeService
	 */
	@Test
	public void startProcess(){
		//启动流程，首先需要获取流程定义的key。通过key才能启动流程。此key就是定义流程时 的id
		String key = "myProcess";
		
		Map<String, Object> variables = new HashMap<>();
		variables.put("days",5);
		variables.put("startime","2017");
		
		//引擎调用对应service
		ProcessInstance processInstance = engine.getRuntimeService()
				//使用流程定义的key，执行流程实例，key对应定义流程实例的bpmn文件中的id属性值	
				.startProcessInstanceByKey(key,variables);
				
		//启动流程时设置变量（流程启动时或者任务完成时设置流程变量。其中流程启动时分为：1  启动之后再设置变量。 2启动的同时设置变量）
		//流程变量的作用域是全局的(一般获取)
		
		
		System.out.println(processInstance.getProcessDefinitionId());//流程定义的id
		System.out.println(processInstance.getId());
		
	}
	
	/**
	 * 第三部：查询任务。当前登陆用户的任务，只有输入当前登录人的名字才能看到自己的任务。当流程执行到某一一个人，那个人才能看到
	 * @Description:
	 * @author: 刘忠伟
	 * @return:void
	 * @time:2017年3月7日 下午12:38:44
	 */
	@Test
	public void findTask(){
		//一个人可能有多个任务(任务肯定是正在执行中的任务)，所以查出来是个list集合更为符合事实
		
		List<Task> tasks = engine.getTaskService()//任务使用task	service
		.createTaskQuery()//创建一个查询
		.taskAssignee("张三")//根据用户查询任务，即条件
		.orderByTaskCreateTime().asc()//查询结果排序根据创建时间
		.list();//结果集
		
		System.out.println(tasks.size());
		
		for(Task task:tasks){
			//获取一下全局的执行变量
			Integer days = (Integer)engine.getRuntimeService().getVariable("42501", "days");
			String time = (String)engine.getRuntimeService().getVariable("42501", "startime");
			System.out.println("name:"+days);
			System.out.println("time:"+time);
		}
		
		
	}
	
	/**
	 * 第四部：完成任务，就是审批办理
	 * @Description:
	 * @author: 刘忠伟
	 * @return:void
	 * @time:2017年3月7日 下午4:22:07
	 */
	@Test
	public void perform(){
		//完成任务，属于任务所以调用taskService。
		//肯定需要任务的id。并且完成任务之后，act_ru_task表（执行时任务表）中肯定关于此任务的记录消失。
		//因为此任务已经结束，不再执行中。历史任务中的此条记录将获取结束时间。（创建实在任务开始的那一刻）
		//创建流程时已经设置任务的分配人，所以只需要任务id。
		String taskId = "12504";
		
		engine.getTaskService().complete(taskId);//里面参数为任务的id。
		//执行完任务可以设置任务流程变量。
		//engine.getTaskService().setVariable("12504", "是否准许", "准许");
		
		
	}
	
	/**
	 * 获取流程定义，根据条件。		流程定义的key是指bpmn文件process节点的id的值，
	 * 							在表中atc_re_procdef表中的key_字段存入
	 * @Description:
	 * @author: 刘忠伟
	 * @return:void
	 * @time:2017年3月7日 下午5:09:49
	 */
	@Test
	public void getDeployment(){
		
		List<ProcessDefinition> definitions = engine.getRepositoryService()
				.createProcessDefinitionQuery()//创建一个流程实例的查询
				.processDefinitionKey("myProcess")
				.list();
		System.out.println(definitions.size());
		for(ProcessDefinition definition:definitions){
			System.out.println(definition.getKey());
		}
		
		//获取任务流程变量。全局的（但是对应的执行中任务id不能没有（不能完成））
		String iString = (String)engine.getTaskService().getVariable("12504", "是否准许");
		
		System.out.println(iString);
	}
	
	
	/**
	 * 查看png图片，即工作流程的图片。		根据部署id和图片名创建输入流，拷贝到输出流即可。即文件下载
	 * @Description:
	 * @author: 刘忠伟
	 * @return:void
	 * @throws  
	 * @time:2017年3月7日 下午7:29:35
	 */
	@Test
	public void PNG()  {
		
		List<ProcessDefinition> deployments = engine.getRepositoryService().createProcessDefinitionQuery()
		.processDefinitionId("myProcess:1:5")
		.list();
		if(deployments != null && deployments.size()>= 0 ){
			for(ProcessDefinition definition:deployments){
				
				String name = definition.getResourceName();//图片名字
				InputStream inputStream = engine.getRepositoryService().getResourceAsStream(definition.getDeploymentId(), name);
				//文件拷贝。使用BUffered的流拷贝更快有缓冲区
				BufferedInputStream inputStream2 = new BufferedInputStream(inputStream);
				try {
					BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File("F:/",name)));
					byte[] bt = new byte[100];
					int len = -1;

					while((len=inputStream2.read(bt)) != -1){
						outputStream.write(bt,0,len);
					}
					outputStream.flush();
					outputStream.close();
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	
	
	
}
