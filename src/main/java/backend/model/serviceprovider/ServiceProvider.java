package backend.model.serviceprovider;

import java.util.HashMap;
import java.util.List;

import backend.model.GlobalPersistenceUnit;
import backend.model.result.Result;
import backend.model.service.GenericService;
import backend.model.service.Service;
import backend.model.service.ServicePersistenceUnit;

public interface ServiceProvider {
	
	public void persistenceUnit(GlobalPersistenceUnit persistenceUnit);
	public GlobalPersistenceUnit persistenceUnit();
	
	// returns a hasmap cansisting of a preconfigured Service and the services description
	HashMap<String, GenericService.ServiceDescriptor> services();
	GenericService.ServiceDescriptor serviceDescriptor(String serviceIdentifier);
	
	// returns the service identified by its Classname
	public <T extends Result> Service<T> service(String serviceName) throws InstantiationException, IllegalAccessException;
	
	public <T extends Result> T executeService(Service<T> serviceToExecute);

}
