package backend.model.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import backend.model.Descriptor;
import backend.model.PluginPersistenceUnit;


public class ServicePersistenceUnit extends PluginPersistenceUnit<ServiceRepository>{
	
	public ServicePersistenceUnit()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Config.xml");
		localRepository(context.getBean(ServiceRepository.class));
	}
	
}
