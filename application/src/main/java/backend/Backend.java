package backend;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import reactor.core.Reactor;
import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;
import backend.model.descriptor.ServiceDescriptor;
import backend.model.result.Result;
import backend.model.service.ServiceEntity;
import backend.model.serviceprovider.GenericServiceProvider;
import backend.model.serviceprovider.ServiceProviderRepository;
import backend.system.GlobalPersistenceUnit;
import backend.system.GlobalState;

public class Backend {

	ServiceProviderRepository m_providerRepository;
	PluginManager m_pluginManager;

	public Backend()
	{
    	ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Config.xml");
    	GlobalState.set("eventReactor", (Reactor) context.getBean("eventReactor"));
	    m_pluginManager = new DefaultPluginManager(new File("3rd_party"));
	
	    GlobalState.set("PluginManager", m_pluginManager);

		GlobalPersistenceUnit persistence = new GlobalPersistenceUnit();
		GlobalState.set("GlobalPersistenceUnit", persistence);

		m_providerRepository = persistence.serviceProviderRepository();
		updatePlugins();
	}

	public ServiceEntity schedule(ServiceDescriptor descriptor, Result configuration)
	{
		ServiceEntity service = null;
		try
		{
			GenericServiceProvider provider = m_providerRepository.findByServiceIdentifier(descriptor.identifier());
			service = provider.service(descriptor.identifier(), configuration);
			provider.executeService(service);
		}
		catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return service;
	}

	public void updatePlugins()
	{
	    m_pluginManager.loadPlugins();
	    m_pluginManager.startPlugins();
	}
}
