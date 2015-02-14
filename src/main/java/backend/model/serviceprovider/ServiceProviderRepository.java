package backend.model.serviceprovider;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import backend.model.GlobalPersistenceUnit;
import backend.model.result.Result;
import backend.model.service.GenericService;
import backend.system.GlobalState;


public class ServiceProviderRepository{
	
	private HashMap<String, GenericServiceProvider.ServiceProviderDescriptor> m_registry;
	private HashMap<String, GenericServiceProvider> m_instances;

	public ServiceProviderRepository()
	{
		m_registry = new HashMap<String, GenericServiceProvider.ServiceProviderDescriptor>();
		m_instances = new HashMap<String, GenericServiceProvider>();
		
    	// create scanner and disable default filters (that is the 'false' argument)
    	final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
    	// add include filters which matches all the classes (or use your own)
    	provider.addIncludeFilter((TypeFilter) new AssignableTypeFilter(GenericServiceProvider.class));
    	// get matching classes defined in the package
    	final Set<BeanDefinition> classes = provider.findCandidateComponents("backend.model");
    	
    	for (BeanDefinition definition : classes) {
			try 
			{
				Class<GenericServiceProvider> registeredClass;
				registeredClass = (Class<GenericServiceProvider>) Class.forName(definition.getBeanClassName());

				GenericServiceProvider instance = registeredClass.newInstance();
				String commonName = instance.commonName();
				String identifier = registeredClass.getCanonicalName();
				MessageDigest messageDigest = MessageDigest.getInstance("SHA");
				messageDigest.update(identifier.getBytes());
				identifier = String.format("%040x", new BigInteger(1, messageDigest.digest()));
				
				m_registry.put(identifier, instance.descriptor());
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			} 
			catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
	
    public GenericServiceProvider serviceProvider(String identifier) throws InstantiationException, IllegalAccessException
    {
    	if(!m_instances.containsKey(identifier))
    		loadInstance(identifier);
    	return m_instances.get(identifier);
    }
    
    public GenericServiceProvider.ServiceProviderDescriptor serviceProviderDescriptor(String identifier) throws InstantiationException, IllegalAccessException
    {
    	return m_registry.get(identifier);
    }
	
	public Iterable<GenericServiceProvider> findAll()
	{
		if(m_registry.size() != m_instances.size())
			loadAllInstances();
			
		return new ArrayList<GenericServiceProvider>(m_instances.values());
	}
	
	public Iterable<GenericServiceProvider.ServiceProviderDescriptor> findAllDescriptors()
	{
		return new ArrayList<GenericServiceProvider.ServiceProviderDescriptor>(m_registry.values());
	}
	
	private void loadInstance(String identifier)
	{
    	Class<GenericServiceProvider> serviceClass = m_registry.get(identifier).classDescriptor();
    	GenericServiceProvider newServiceProvider;
		try 
		{
			newServiceProvider = (GenericServiceProvider) serviceClass.newInstance();
	    	
	    	newServiceProvider.persistenceUnit((GlobalPersistenceUnit) GlobalState.get("GlobalPersistenceUnit"));
	    	    	
	    	m_instances.put(identifier, newServiceProvider);
		} 
		catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void loadAllInstances()
	{
		for (String identifier : m_registry.keySet()) {
	    	if(!m_instances.containsKey(identifier))
	    		loadInstance(identifier);
		}
	}
	
}
