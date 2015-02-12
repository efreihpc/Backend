package backend.system;

import backend.model.service.ServiceRepository;

public interface ServicePersistenceUnit {
	ServiceRepository serviceRepository();
}
