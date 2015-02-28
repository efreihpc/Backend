package backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.web.bind.annotation.RequestBody;

import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;
import backend.model.GlobalPersistenceUnit;
import backend.model.SPHPC.FinanceServiceProvider;
import backend.model.service.ServiceEntity;
import backend.model.service.Service;
import backend.model.service.ServicePlugin;
import backend.model.service.ServiceRepository;
import backend.model.serviceprovider.GenericServiceProvider;
import backend.model.serviceprovider.ServiceProviderRepository;
import backend.system.GlobalState;
import backend.system.JoinClassLoader;
import backend.system.PluginEntityManagerFactory;

public class Backend {
	
	ServiceProviderRepository m_providerRepository;
	PluginManager m_pluginManager;
	
	public Backend()
	{
	    m_pluginManager = new DefaultPluginManager(new File("3rd_party"));
	    
	    GlobalState.set("PluginManager", m_pluginManager);
		
		GlobalPersistenceUnit persistence = new GlobalPersistenceUnit();
		GlobalState.set("GlobalPersistenceUnit", persistence);
			
		m_providerRepository = persistence.serviceProviderRepository();
		
		updatePlugins();
	}
	
	public void schedule(ServiceEntity.ServiceDescriptor descriptor)
	{
		try {
			GenericServiceProvider provider = m_providerRepository.serviceProvider(descriptor.providerIdentifier());
			ServiceEntity service = provider.service(descriptor.identifier());
			provider.executeService(service);
		} 
		catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updatePlugins()
	{
	    m_pluginManager.loadPlugins();
	    m_pluginManager.startPlugins();
	}
}
