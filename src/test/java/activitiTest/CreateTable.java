package activitiTest;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class CreateTable {
	// 使用代码创建工作流需要的23张表
	@Test
	public void createTable(){
		
		 // 工作流引擎的全部配置  
	    ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
	            .createStandaloneProcessEngineConfiguration();  
	  
	    // 链接数据的配置  
	    processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");  
	    processEngineConfiguration  
	            .setJdbcUrl("jdbc:mysql://localhost:3306/aaa?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf8");
	    processEngineConfiguration.setJdbcUsername("root");  
	    processEngineConfiguration.setJdbcPassword("1234");  
	   
	    /* 
	     * public static final String DB_SCHEMA_UPDATE_FALSE = "false"; 
	     * 不能自动创建表，需要表存在 public static final String DB_SCHEMA_UPDATE_CREATE_DROP 
	     * = "create-drop"; 先删除表再创建表 public static final String 
	     * DB_SCHEMA_UPDATE_TRUE = "true";如果表不存在，自动创建表 
	     */  
	    //如果表不存在，自动创建表  
	    processEngineConfiguration  
	            .setDatabaseSchemaUpdate(processEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);  
	    // 工作流的核心对象，ProcessEnginee对象  
	    ProcessEngine processEngine = processEngineConfiguration
	            .buildProcessEngine();  
	    System.out.println(processEngine);  
		
	}
	

	/** 
	 * 使用配置文件来创建数据库中的表 
	 */  
	@Test  
	public void createTable_2() {  
	    //通过让工作流引擎的全部配置对象来执行配置文件中的内容来创建流程引擎对象  
	    ProcessEngine processEngine = ProcessEngineConfiguration  
	            .createProcessEngineConfigurationFromResource(  
	                    "activiti.cfg.xml").buildProcessEngine();
	    System.out.println("processEngine" + processEngine);  
	}  

}
