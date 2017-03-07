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
* @author: 刘忠伟 
* @date: 2017年3月7日 下午9:26:40 
* @version 1.0
* 
*/

public class Activiti {
	
	private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
	
	
	
	/**
	 * 部署流程，定义
	 * @Description:
	 * @author: 刘忠伟
	 * @return:void
	 * @time:2017年3月7日 下午9:27:39
	 */
	@Test
	public void repository(){
		Deployment deployment = engine.getRepositoryService()
				.createDeployment()
				.name("排他网管测试")
				.addClasspathResource("diagrams/MyProcess3.bpmn")
				.addClasspathResource("diagrams/MyProcess3.png")
				.deploy();
		
	}

}
