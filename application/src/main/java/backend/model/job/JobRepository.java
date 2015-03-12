package backend.model.job;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<JobEntity, Long> {
	@Query("select s from JobEntity s where s.m_id = :id")
	abstract List<JobEntity> findById(@Param("id") long id);
	
	@Query("select s from JobEntity s where s.m_classLoader = :cl")
	abstract List<JobEntity> findByClassLoader(@Param("cl") String cl);
}
