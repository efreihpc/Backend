package backend.model.service;

import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<GenericService, Long>{
//	List<GenericService> findByUid(String name);
}
