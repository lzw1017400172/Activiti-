/**
 * 
 */
package com.kaishengit;

import java.util.List;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

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
	 * 启动流程实例，使用RuntimeService
	 */
	@Test
	public void startProcess(){
		//启动流程，首先需要获取流程定义的key。通过key才能启动流程。此key就是定义流程时 的id
		String key = "myProcess";
		
		//引擎调用对应service
		ProcessInstance processInstance = engine.getRuntimeService()
				//使用流程定义的key，执行流程实例，key对应定义流程实例的bpmn文件中的id属性值	
				.startProcessInstanceByKey(key);
				
		System.out.println(processInstance.getProcessDefinitionId());//流程定义的id
		System.out.println(processInstance.getId());
		
	}
	
	/**
	 * 查询任务。当前登陆用户的任务，只有输入当前登录人的名字才能看到自己的任务。当流程执行到某一一个人，那个人才能看到
	 * @Description:
	 * @author: 刘忠伟
	 * @return:void
	 * @time:2017年3月7日 下午12:38:44
	 */
	@Test
	public void findTask(){
		//一个人可能有多个任务，所以查出来是个list集合更为符合事实
		
		List<Task> tasks = engine.getTaskService()//任务使用task	service
		.createTaskQuery()//创建一个查询
		.taskAssignee("张三")//根据用户查询任务，即条件
		.orderByTaskCreateTime().asc()//查询结果排序根据创建时间
		.list();//结果集
		
		System.out.println(tasks.size());
		
		for(Task task:tasks){
			System.out.println("name:"+task.getName());
			System.out.println("id:"+task.getId());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
