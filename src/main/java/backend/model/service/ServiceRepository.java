package backend.model.service;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ServiceRepository extends CrudRepository<GenericService, Long>{
	
	@Query("select s from GenericService s where s.m_id = :id")
	List<GenericService> findById(@Param("id") long id);
}
