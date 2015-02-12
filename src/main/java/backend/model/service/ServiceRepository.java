package backend.model.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<GenericService, Long>{
//	List<GenericService> findByUid(String name);
}
