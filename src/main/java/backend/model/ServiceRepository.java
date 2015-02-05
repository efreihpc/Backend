package backend.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<GenericService, Long>{
	List<GenericService> findByName(String name);
}
