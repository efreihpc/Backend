package backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

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
	
	public Backend()
	{
	    PluginManager pluginManager = new DefaultPluginManager(new File("3rd_party"));
	    pluginManager.loadPlugins();
	    pluginManager.startPlugins();
	    
	    GlobalState.set("PluginManager", pluginManager);
		
		GlobalPersistenceUnit persistence = new GlobalPersistenceUnit();
		GlobalState.set("GlobalPersistenceUnit", persistence);
	}
	
//	public void schedule(String serviceIdentifier)
//	{
//		Service service;
//		try {
//			service = m_serviceProvider.service(serviceIdentifier);
//			m_serviceProvider.executeService(service);
//		} 
//		catch (InstantiationException | IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
