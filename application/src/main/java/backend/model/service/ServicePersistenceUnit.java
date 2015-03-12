package backend.model.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import backend.system.PluginPersistenceUnit;


public class ServicePersistenceUnit extends PluginPersistenceUnit<ServiceEntity>{
	
	public ServicePersistenceUnit()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Config.xml");
		localRepository(context.getBean(ServiceRepository.class));
	}
	
	public ServiceRepository localServiceRepository()
	{
		return (ServiceRepository) localRepository();
	}
	
	public ServiceRepository pluginServiceRepository()
	{
		return (ServiceRepository) pluginRepository();
	}
	
}
