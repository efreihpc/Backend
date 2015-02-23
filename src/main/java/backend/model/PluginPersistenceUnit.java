package backend.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class PluginPersistenceUnit<T extends JpaRepository> implements PersistenceUnit<T>{
	
	private HashMap<String, Pair<T, EntityManager>> m_pluginRepositories;
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
	
	public void addPluginRepository(String identifier, T repository, EntityManager em)
	{
		if(!m_pluginRepositories.containsKey(identifier))
			m_pluginRepositories.put(identifier, new Pair<T, EntityManager>(repository, em));
	}
	
	public Pair<T, EntityManager> repository(String pluginIdentifier)
	{
		if(pluginIdentifier.equals("Default"))
			return new Pair<T, EntityManager>(m_localRepository, null);
		
		return m_pluginRepositories.get(pluginIdentifier);
	}
	
	public Pair<T, EntityManager> repository(Descriptor descriptor)
	{
		return repository(descriptor.pluginIdentifier());
	}
}