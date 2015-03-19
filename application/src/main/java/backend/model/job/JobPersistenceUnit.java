package backend.model.job;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import backend.system.PluginPersistenceUnit;


public class JobPersistenceUnit extends PluginPersistenceUnit<JobEntity>{
	
	public JobPersistenceUnit()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Config.xml");
		localRepository(context.getBean(JobRepository.class));
	}
	
	public JobRepository localServiceRepository()
	{
		return (JobRepository) localRepository();
	}
	
	public JobRepository pluginServiceRepository()
	{
		return (JobRepository) pluginRepository();
	}
	
}