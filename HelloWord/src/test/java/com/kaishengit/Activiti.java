/**
 * 
 */
package com.kaishengit;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.h2.engine.Engine;
import org.junit.Test;

/** 
* @ClassName: Activiti 
* @Description: 
* @author: ����ΰ 
* @date: 2017��3��7�� ����9:26:40 
* @version 1.0
* 
*/

public class Activiti {
	
	private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	
	
	
	/**
	 * �������̣�����
	 * @Description:
	 * @author: ����ΰ
	 * @return:void
	 * @time:2017��3��7�� ����9:27:39
	 */
	@Test
	public void repository(){
		Deployment deployment = engine.getRepositoryService()
				.createDeployment()
				.name("�������ܲ���")
				.addClasspathResource("diagrams/MyProcess3.bpmn")
				.addClasspathResource("diagrams/MyProcess3.png")
				.deploy();
		
	}

}
