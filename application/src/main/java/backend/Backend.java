package backend;

import java.io.File;

import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;
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
    m_pluginManager = new DefaultPluginManager(new File("3rd_party"));

    GlobalState.set("PluginManager", m_pluginManager);

		GlobalPersistenceUnit persistence = new GlobalPersistenceUnit();
		GlobalState.set("GlobalPersistenceUnit", persistence);

		m_providerRepository = persistence.serviceProviderRepository();
	}

	public void schedule(ServiceEntity.ServiceDescriptor descriptor)
	{
		try
		{
			GenericServiceProvider provider = m_providerRepository.serviceProvider(descriptor.providerIdentifier());
			ServiceEntity service = provider.service(descriptor.identifier());
			provider.executeService(service);
		}
		catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void updatePlugins()
	{
	    m_pluginManager.loadPlugins();
	    m_pluginManager.startPlugins();
	}
}
