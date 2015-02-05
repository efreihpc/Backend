package backend.system;

import backend.model.ServiceRepository;

public interface ServicePersistenceUnit {
	ServiceRepository serviceRepository();
}
