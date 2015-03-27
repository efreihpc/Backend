package backend.model.serviceprovider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import backend.system.GlobalPersistenceUnit;
import backend.system.GlobalState;


public class ServiceProviderRepository{
	
	private HashMap<String, GenericServiceProvider.ServiceProviderDescriptor> m_registry;
	private HashMap<String, GenericServiceProvider> m_instances;

	public ServiceProviderRepository()
	{
		m_registry = new HashMap<String, GenericServiceProvider.ServiceProviderDescriptor>();
		m_instances = new HashMap<String, GenericServiceProvider>();
		registerLocalServiceProviders();
	}
	
	private void registerLocalServiceProviders()
	{	
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
				
				m_registry.put(instance.descriptor().identifier(), instance.descriptor());
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
			}
    	}
	}
	
	public void registerServiceProvider(GenericServiceProvider provider)
	{
		m_registry.put(provider.descriptor().identifier(), provider.descriptor());
		m_instances.put(provider.descriptor().identifier(), provider);
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
	
	public List<GenericServiceProvider> findAll()
	{
		if(m_registry.size() != m_instances.size())
			loadAllInstances();
			
		return new ArrayList<GenericServiceProvider>(m_instances.values());
	}
	
	public GenericServiceProvider findByServiceIdentifier(String identifier)
	{
		List<GenericServiceProvider> allProviders = findAll();
		for(GenericServiceProvider provider: allProviders)
		{
			if(provider.hasService(identifier))
				return provider;
		}
		
		return null;
	}
	
	public List<GenericServiceProvider.ServiceProviderDescriptor> findAllDescriptors()
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
