package backend.model;

import java.util.List;

import backend.system.ServicePersistenceUnit;

public interface ServiceProvider {
	
	public void persistenceUnit(ServicePersistenceUnit persistenceUnit);
	public ServicePersistenceUnit persistenceUnit();
	
	// returns a hasmap cansisting of a preconfigured Service and the services description
	List<String> services();
	
	// returns the service identified by its Classname
	public <T> Service<T> service(String serviceName) throws InstantiationException, IllegalAccessException;
	
	public <T> T executeService(Service<T> serviceToExecute);

}
