package backend.system;

import backend.model.serviceprovider.ServiceProviderRepository;

public interface ServiceProviderPersistenceUnit {
	ServiceProviderRepository serviceProviderRepository();
}
