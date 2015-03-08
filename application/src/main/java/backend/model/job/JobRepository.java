package backend.model.job;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends CrudRepository<JobEntity, Long> {
	@Query("select j from JobEntity j where j.m_id = :id")
	List<JobEntity> findById(@Param("id") long id);
}
