package backend.model.result;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ResultRepository extends CrudRepository<Result, Long>{
	@Query("select s from GenericService s where s.m_id = :id")
	List<Result> findById(@Param("id") long id);
}
