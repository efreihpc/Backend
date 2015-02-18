package backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;
import backend.model.GlobalPersistenceUnit;
import backend.model.SPHPC.FinanceServiceProvider;
import backend.model.service.ServiceEntity;
import backend.model.service.Service;
import backend.model.serviceprovider.GenericServiceProvider;
import backend.model.serviceprovider.ServiceProviderRepository;
import backend.system.GlobalState;

public class Backend {
	
	ServiceProviderRepository m_providerRepository;
	
	public Backend()
	{
	    PluginManager pluginManager = new DefaultPluginManager(new File("3rd_party"));
	    pluginManager.loadPlugins();
	    pluginManager.startPlugins();
	    
	    GlobalState.set("PluginManager", pluginManager);
		
		GlobalPersistenceUnit persistence = new GlobalPersistenceUnit();
		GlobalState.set("GlobalPersistenceUnit", persistence);
		
		m_providerRepository = persistence.serviceProviderRepository();
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
}
