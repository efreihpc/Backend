package backend.system;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import backend.model.descriptor.Describable;

public abstract class PluginPersistenceUnit<T extends Describable> implements PersistenceUnit<T>{
	
	private JpaRepository<T, Long> m_pluginRepository;
	private EntityManager m_pluginEntityManager;
	private JpaRepository<T, Long> m_localRepository;
	private ClassLoader m_pluginClassLoader;
	
	public PluginPersistenceUnit()
	{
		m_pluginClassLoader = this.getClass().getClassLoader();
	}
	
	protected void localRepository(JpaRepository<T, Long> repository)
	{
		m_localRepository = repository;
	}
	
	public void addPluginRepository(JpaRepository<T, Long> repository, EntityManager em)
	{
		m_pluginRepository = repository;
		m_pluginEntityManager = em;
	}
	
	@Override
	public <U extends JpaRepository<T, Long>>void registerPluginRepository(ClassLoader loader, Class<U> repositoryType)
	{
	    m_pluginClassLoader = new JoinClassLoader(m_pluginClassLoader, m_pluginClassLoader, loader);
	    
	    PluginEntityManagerFactory factory = new PluginEntityManagerFactory(m_pluginClassLoader);
	    EntityManager em = factory.createEntityManager();
	    JpaRepositoryFactory repositoryFactory = new JpaRepositoryFactory(em);
	    JpaRepository<T, Long> repository = repositoryFactory.getRepository(repositoryType);

	    addPluginRepository(repository, em);
	}
	
	@Override
	public JpaRepository<T, Long> pluginRepository()
	{
		return m_pluginRepository;
	}
	
	@Override
	public EntityManager pluginEntityManager()
	{
		return m_pluginEntityManager;
	}
	
	@Override
	public JpaRepository<T, Long> localRepository()
	{
		return m_localRepository;
	}
	
	public void save(T instance)
	{		
		// Default Repository
		if(!instance.descriptor().pluginIdentifier())
		{
			m_localRepository.save(instance);
			return;
		}
		
		m_pluginEntityManager.getTransaction().begin();
		m_pluginRepository.save(instance);
		m_pluginEntityManager.getTransaction().commit();
	}
	
	public void delete(T instance)
	{
		// Default Repository
		if(!instance.descriptor().pluginIdentifier())
		{
			m_localRepository.save(instance);
			return;
		}
		
		m_pluginEntityManager.getTransaction().begin();
		m_pluginRepository.delete(instance);
		m_pluginEntityManager.getTransaction().commit();
	}
}