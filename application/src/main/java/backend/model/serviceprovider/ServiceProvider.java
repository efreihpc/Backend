package backend.model.serviceprovider;

import java.util.HashMap;

import backend.model.descriptor.ServiceDescriptor;
import backend.model.result.Result;
import backend.model.service.Service;
import backend.model.service.ServiceEntity;
import backend.system.GlobalPersistenceUnit;

public interface ServiceProvider {
	
	public void persistenceUnit(GlobalPersistenceUnit persistenceUnit);
	public GlobalPersistenceUnit persistenceUnit();
	
	// returns a hasmap cansisting of a preconfigured Service and the services description
	HashMap<String, ServiceDescriptor> services();
	ServiceDescriptor serviceDescriptor(String serviceIdentifier);
	
	// returns the service identified by its Classname
	public <T extends Result> Service<T> service(String serviceName) throws InstantiationException, IllegalAccessException;
	
	public <T extends Result> void executeService(ServiceEntity<T> serviceToExecute);

}
