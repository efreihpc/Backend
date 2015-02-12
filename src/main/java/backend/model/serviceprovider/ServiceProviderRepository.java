package backend.model.serviceprovider;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ServiceProviderRepository extends CrudRepository<GenericServiceProvider, Long>{
//	List<GenericServiceProvider> findByName(String name);
}
