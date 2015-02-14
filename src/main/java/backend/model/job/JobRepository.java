package backend.model.job;

import org.springframework.data.repository.CrudRepository;

public interface JobRepository extends CrudRepository<GenericJob, Long> {
//	List<GenericJob> findByName(String name);
}
