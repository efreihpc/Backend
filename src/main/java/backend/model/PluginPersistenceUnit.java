package backend.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class PluginPersistenceUnit<T extends Describable> implements PersistenceUnit<T>{
	
	private JpaRepository<T, Long> m_pluginRepository;
	private EntityManager m_pluginEntityManager;
	private JpaRepository<T, Long> m_localRepository;
	
	public PluginPersistenceUnit()
	{}
	
	protected void localRepository(JpaRepository<T, Long> repository)
	{
		m_localRepository = repository;
	}
	
	public void addPluginRepository(JpaRepository<T, Long> repository, EntityManager em)
	{
		m_pluginRepository = repository;
		m_pluginEntityManager = em;
	}
	
	public Pair<JpaRepository<T, Long>, EntityManager> pluginRepository()
	{
		return new Pair<JpaRepository<T, Long>, EntityManager>(m_pluginRepository, m_pluginEntityManager);
	}
	
	public JpaRepository<T, Long> localRepository()
	{
		return m_localRepository;
	}
	
	public Pair<JpaRepository<T, Long>, EntityManager> repository(Describable instance)
	{
		System.out.println(m_pluginRepository);
		System.out.println(m_pluginEntityManager);
		if(instance.descriptor().pluginIdentifier())
			return new Pair<JpaRepository<T, Long>, EntityManager>(m_pluginRepository, m_pluginEntityManager);
		else
			return new Pair<JpaRepository<T, Long>, EntityManager>(m_localRepository, null);

	}
	
	// TODO: Repository should be updated with return value of save
	public void save(T instance)
	{
		Pair<JpaRepository<T, Long>, EntityManager> pair = repository(instance);
		
		// Classloader does not exist
		if(pair == null)
			return;
		
		// Default Repository
		if(pair.right() == null)
		{
			pair.left().save(instance);
			return;
		}
		
		System.out.println("Persisting " + instance.descriptor().commonName());
		pair.right().getTransaction().begin();
		pair.left().save(instance);
		pair.right().getTransaction().commit();
	}
	
	// TODO: Repository should be updated with return value of delete
	public void delete(T instance)
	{
		Pair<JpaRepository<T, Long>, EntityManager> pair = repository(instance);
		
		// Classloader does not exist
		if(pair == null)
			return;
		
		System.out.println(pair.left());
		System.out.println(pair.right());
		
		// Default Repository
		if(pair.right() == null)
		{
			pair.left().save(instance);
			return;
		}
			
		pair.right().getTransaction().begin();
		pair.left().delete(instance);
		pair.right().getTransaction().commit();
	}
}