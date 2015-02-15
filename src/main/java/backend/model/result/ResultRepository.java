package backend.model.result;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ResultRepository extends CrudRepository<Result, Long>{
	@Query("select r from Result r where r.m_id = :id")
	List<Result> findById(@Param("id") long id);
}
