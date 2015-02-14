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
				m_instances.put(identifier, instance);
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
	
    public GenericServiceProvider serviceProvider(String serviceProviderName) throws InstantiationException, IllegalAccessException
    {
    	return m_instances.get(serviceProviderName);
    }
    
    public GenericServiceProvider.ServiceProviderDescriptor serviceProviderDescriptor(String serviceProviderName) throws InstantiationException, IllegalAccessException
    {
    	return m_registry.get(serviceProviderName);
    }
	
	public Iterable<GenericServiceProvider> findAll()
	{
		return new ArrayList<GenericServiceProvider>(m_instances.values());
	}
	
	public Iterable<GenericServiceProvider.ServiceProviderDescriptor> findAllDescriptors()
	{
		return new ArrayList<GenericServiceProvider.ServiceProviderDescriptor>(m_registry.values());
	}
	
}
