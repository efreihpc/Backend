package backend.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class PluginPersistenceUnit<T extends JpaRepository> implements PersistenceUnit<T>{
	
	private HashMap<String, T> m_pluginRepositories;
	T m_localRepository;
	
	public PluginPersistenceUnit()
	{}
	
	protected void localRepository(T repository)
	{
		m_localRepository = repository;
	}
	
	public boolean hasPlugin(String identifier)
	{
		return m_pluginRepositories.containsKey(identifier);
	}
	
	public void addPluginRepository(String identifier, T repository)
	{
		if(!m_pluginRepositories.containsKey(identifier))
			m_pluginRepositories.put(identifier, repository);
	}
	
	public T repository(String pluginIdentifier)
	{
		if(pluginIdentifier == null)
			return m_localRepository;
		
		return m_pluginRepositories.get(pluginIdentifier);
	}
	
	public T repository(Descriptor descriptor)
	{
		return repository(descriptor.pluginIdentifier());
	}
}