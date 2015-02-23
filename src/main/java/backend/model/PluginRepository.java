package backend.model;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.model.service.ServiceEntity;

public abstract class PluginRepository<T> implements JpaRepository<T, Long> {
	
	EntityManager m_entityManager; 
	
	public void entityManager(EntityManager entityManager)
	{
		m_entityManager = entityManager;
	}
	
	public PluginRepository<T> compSave(T entity)
	{
		if(m_entityManager == null)
			return (PluginRepository<T>) save(entity);
		
		m_entityManager.getTransaction().begin();
		PluginRepository<T> newRepo = (PluginRepository<T>) save(entity);
		m_entityManager.getTransaction().commit();
		
		return newRepo;
		
	}

}
