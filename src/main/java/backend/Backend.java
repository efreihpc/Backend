package backend;

import java.util.HashMap;
import java.util.List;

import backend.model.GlobalPersistenceUnit;
import backend.model.SPHPC.FinanceServiceProvider;
import backend.model.service.ServiceEntity;
import backend.model.service.Service;
import backend.system.GlobalState;

public class Backend {
	
	FinanceServiceProvider m_serviceProvider;

	public Backend()
	{
		m_serviceProvider = new FinanceServiceProvider();
		
		GlobalPersistenceUnit persistence = new GlobalPersistenceUnit();
		m_serviceProvider.persistenceUnit(persistence);
		GlobalState.set("GlobalPersistenceUnit", persistence);
	}
	
	public HashMap<String, ServiceEntity.ServiceDescriptor> services()
	{
		return m_serviceProvider.services();
	}
	
	public ServiceEntity.ServiceDescriptor serviceDescriptor(String serviceIdentifier)
	{
		return m_serviceProvider.serviceDescriptor(serviceIdentifier);
	}
	
	public void schedule(String serviceIdentifier)
	{
		Service service;
		try {
			service = m_serviceProvider.service(serviceIdentifier);
			m_serviceProvider.executeService(service);
		} 
		catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
