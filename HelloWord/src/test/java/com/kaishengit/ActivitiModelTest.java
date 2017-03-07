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
* @author: ����ΰ 
* @date: 2017��3��7�� ����9:20:28 
* @version 1.0
* 
*/

public class ActivitiModelTest {

	
	/**
	 * ��ʼ�����ݿ⣬�����б��ʼ����mysql///�������Ŀ�п϶���Ҫд�ڴ�����У�ֻ����һ��
	 * ����Ҫxml�ļ�
	 * @Description:
	 * @author: ����ΰ
	 * @return:void
	 * @time:2017��3��7�� ����9:21:28
	 */
	@Test
	public void initMysql(){
		//����һ���������ݿ�����
		ProcessEngineConfiguration configuration = 
				ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		//����mysql
		configuration.setJdbcDriver("com.mysql.jdbc.Driver");
		configuration.setJdbcUrl("jdbc:mysql:///activiti_model");
		configuration.setJdbcUsername("root");
		configuration.setJdbcPassword("root");
		
		configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);//�����ھ��Զ�������
		
		//����������󣬴������ų�ʼ���������
		ProcessEngine engine  = configuration.buildProcessEngine();
		System.out.println(engine.getName());
	}
	
	/**
	 * mysql�ı��ĳ�ʼ����ʹ��spring����Ҫxml�ļ�����Ҫ�ڴ�����setע�롣
	 * @Description:
	 * @author: ����ΰ
	 * @return:void
	 * @time:2017��3��7�� ����10:24:01
	 */
	@Test
	public void initMysqlBySpring(){
		//springע�����ݿ����ú�ֻ��Ҫ����������󣬼��ɳ�ʼ�����
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		System.out.println(engine.getName());
	}
	
	
	
	
	
	//Activiti��servic	API,�ߴ�Service�ӿڡ�
	//����service�ӿڵĵ��ö���ͨ������ģ����Դ���һ������
	private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	
	
	/**
	 * ��һ�����������̣���������
	 * @Description:
	 * @author: ����ΰ
	 * @return:void
	 * @time:2017��3��7�� ����11:17:37
	 */
	@Test
	public void deployment(){
		//�������̣���������붨��������ص�service��
		Deployment deployment = engine.getRepositoryService()
				.createDeployment()//����һ���������̶���
				.name("hello!����")//��Ӷ������̵����֣���������
				.addClasspathResource("diagrams/MyProcess1.bpmn")//��classpath�м�����Դ��һ��ֻ�ܼ���һ��
				.addClasspathResource("diagrams/Myprocess1.png")//�������̶������ͼƬ��Ҫ����
				.deploy();//����
		
		System.out.println(deployment.getName());
		System.out.println(deployment.getId());
	}
	
	/**
	 * ��������ʵ����ʹ��RuntimeService
	 */
	@Test
	public void startProcess(){
		//�������̣�������Ҫ��ȡ���̶����key��ͨ��key�����������̡���key���Ƕ�������ʱ ��id
		String key = "myProcess";
		
		//������ö�Ӧservice
		ProcessInstance processInstance = engine.getRuntimeService()
				//ʹ�����̶����key��ִ������ʵ����key��Ӧ��������ʵ����bpmn�ļ��е�id����ֵ	
				.startProcessInstanceByKey(key);
				
		System.out.println(processInstance.getProcessDefinitionId());//���̶����id
		System.out.println(processInstance.getId());
		
	}
	
	/**
	 * ��ѯ���񡣵�ǰ��½�û�������ֻ�����뵱ǰ��¼�˵����ֲ��ܿ����Լ������񡣵�����ִ�е�ĳһһ���ˣ��Ǹ��˲��ܿ���
	 * @Description:
	 * @author: ����ΰ
	 * @return:void
	 * @time:2017��3��7�� ����12:38:44
	 */
	@Test
	public void findTask(){
		//һ���˿����ж���������Բ�����Ǹ�list���ϸ�Ϊ������ʵ
		
		List<Task> tasks = engine.getTaskService()//����ʹ��task	service
		.createTaskQuery()//����һ����ѯ
		.taskAssignee("����")//�����û���ѯ���񣬼�����
		.orderByTaskCreateTime().asc()//��ѯ���������ݴ���ʱ��
		.list();//�����
		
		System.out.println(tasks.size());
		
		for(Task task:tasks){
			System.out.println("name:"+task.getName());
			System.out.println("id:"+task.getId());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
