package backend.system;

import backend.model.ServiceProviderRepository;

public interface ServiceProviderPersistenceUnit {
	ServiceProviderRepository serviceProviderRepository();
}
