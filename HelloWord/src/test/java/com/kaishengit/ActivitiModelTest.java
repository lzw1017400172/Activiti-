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
	 * ��zipѹ��������ʽ���������̡�	����zip����zip�������������������𼴿ɡ�
	 * @Description:
	 * @author: ����ΰ
	 * @return:void
	 * @time:2017��3��7�� ����4:51:50
	 */
	@Test
	public void zipDeployment(){
		//����zip�ļ���������
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("diagrams/MyProcess2.zip");
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		
		//�Ƕ�����������ʹ��Repositoryservice
		Deployment deployment = engine.getRepositoryService()
				.createDeployment()//����һ���������
				.name("�������")//�������̵�����
				.addZipInputStream(zipInputStream)
				.deploy();//��ɲ���
		
		System.out.println(deployment.getName());
	}
	
	
	/**
	 * �ڶ�������������ʵ����ʹ��RuntimeService
	 */
	@Test
	public void startProcess(){
		//�������̣�������Ҫ��ȡ���̶����key��ͨ��key�����������̡���key���Ƕ�������ʱ ��id
		String key = "myProcess";
		
		Map<String, Object> variables = new HashMap<>();
		variables.put("days",5);
		variables.put("startime","2017");
		
		//������ö�Ӧservice
		ProcessInstance processInstance = engine.getRuntimeService()
				//ʹ�����̶����key��ִ������ʵ����key��Ӧ��������ʵ����bpmn�ļ��е�id����ֵ	
				.startProcessInstanceByKey(key,variables);
				
		//��������ʱ���ñ�������������ʱ�����������ʱ�������̱�����������������ʱ��Ϊ��1  ����֮�������ñ����� 2������ͬʱ���ñ�����
		//���̱�������������ȫ�ֵ�(һ���ȡ)
		
		
		System.out.println(processInstance.getProcessDefinitionId());//���̶����id
		System.out.println(processInstance.getId());
		
	}
	
	/**
	 * ����������ѯ���񡣵�ǰ��½�û�������ֻ�����뵱ǰ��¼�˵����ֲ��ܿ����Լ������񡣵�����ִ�е�ĳһһ���ˣ��Ǹ��˲��ܿ���
	 * @Description:
	 * @author: ����ΰ
	 * @return:void
	 * @time:2017��3��7�� ����12:38:44
	 */
	@Test
	public void findTask(){
		//һ���˿����ж������(����϶�������ִ���е�����)�����Բ�����Ǹ�list���ϸ�Ϊ������ʵ
		
		List<Task> tasks = engine.getTaskService()//����ʹ��task	service
		.createTaskQuery()//����һ����ѯ
		.taskAssignee("����")//�����û���ѯ���񣬼�����
		.orderByTaskCreateTime().asc()//��ѯ���������ݴ���ʱ��
		.list();//�����
		
		System.out.println(tasks.size());
		
		for(Task task:tasks){
			//��ȡһ��ȫ�ֵ�ִ�б���
			Integer days = (Integer)engine.getRuntimeService().getVariable("42501", "days");
			String time = (String)engine.getRuntimeService().getVariable("42501", "startime");
			System.out.println("name:"+days);
			System.out.println("time:"+time);
		}
		
		
	}
	
	/**
	 * ���Ĳ���������񣬾�����������
	 * @Description:
	 * @author: ����ΰ
	 * @return:void
	 * @time:2017��3��7�� ����4:22:07
	 */
	@Test
	public void perform(){
		//������������������Ե���taskService��
		//�϶���Ҫ�����id�������������֮��act_ru_task��ִ��ʱ������п϶����ڴ�����ļ�¼��ʧ��
		//��Ϊ�������Ѿ�����������ִ���С���ʷ�����еĴ�����¼����ȡ����ʱ�䡣������ʵ������ʼ����һ�̣�
		//��������ʱ�Ѿ���������ķ����ˣ�����ֻ��Ҫ����id��
		String taskId = "12504";
		
		engine.getTaskService().complete(taskId);//�������Ϊ�����id��
		//ִ����������������������̱�����
		//engine.getTaskService().setVariable("12504", "�Ƿ�׼��", "׼��");
		
		
	}
	
	/**
	 * ��ȡ���̶��壬����������		���̶����key��ָbpmn�ļ�process�ڵ��id��ֵ��
	 * 							�ڱ���atc_re_procdef���е�key_�ֶδ���
	 * @Description:
	 * @author: ����ΰ
	 * @return:void
	 * @time:2017��3��7�� ����5:09:49
	 */
	@Test
	public void getDeployment(){
		
		List<ProcessDefinition> definitions = engine.getRepositoryService()
				.createProcessDefinitionQuery()//����һ������ʵ���Ĳ�ѯ
				.processDefinitionKey("myProcess")
				.list();
		System.out.println(definitions.size());
		for(ProcessDefinition definition:definitions){
			System.out.println(definition.getKey());
		}
		
		//��ȡ�������̱�����ȫ�ֵģ����Ƕ�Ӧ��ִ��������id����û�У�������ɣ���
		String iString = (String)engine.getTaskService().getVariable("12504", "�Ƿ�׼��");
		
		System.out.println(iString);
	}
	
	
	/**
	 * �鿴pngͼƬ�����������̵�ͼƬ��		���ݲ���id��ͼƬ����������������������������ɡ����ļ�����
	 * @Description:
	 * @author: ����ΰ
	 * @return:void
	 * @throws  
	 * @time:2017��3��7�� ����7:29:35
	 */
	@Test
	public void PNG()  {
		
		List<ProcessDefinition> deployments = engine.getRepositoryService().createProcessDefinitionQuery()
		.processDefinitionId("myProcess:1:5")
		.list();
		if(deployments != null && deployments.size()>= 0 ){
			for(ProcessDefinition definition:deployments){
				
				String name = definition.getResourceName();//ͼƬ����
				InputStream inputStream = engine.getRepositoryService().getResourceAsStream(definition.getDeploymentId(), name);
				//�ļ�������ʹ��BUffered�������������л�����
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
