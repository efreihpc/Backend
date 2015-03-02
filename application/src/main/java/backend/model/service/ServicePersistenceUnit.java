package backend.model.service;

import javax.persistence.EntityManager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import backend.model.Describable;
import backend.model.Descriptor;
import backend.model.Pair;
import backend.model.PluginPersistenceUnit;


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
