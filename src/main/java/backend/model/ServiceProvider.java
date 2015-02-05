package backend.model;

import java.util.HashMap;

import backend.system.ServicePersistenceUnit;

public interface ServiceProvider {
	
	public void persistenceUnit(ServicePersistenceUnit persistenceUnit);
	
	// returns a hasmap cansisting of a preconfigured Service and the services description
	HashMap<Service, String> services();
	
	void executeService(Service serviceToExecute);

}
