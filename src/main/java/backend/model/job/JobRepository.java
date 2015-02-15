package backend.model.job;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends CrudRepository<GenericJob, Long> {
	@Query("select s from GenericService s where s.m_id = :id")
	List<GenericJob> findById(@Param("id") long id);
}
