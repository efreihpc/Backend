package backend.model.serviceprovider;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import ro.fortsoft.pf4j.ExtensionPoint;
import backend.model.Descriptor;
import backend.model.GlobalPersistenceUnit;
import backend.model.job.JobExecutor;
import backend.model.result.Result;
import backend.model.service.Service;
import backend.model.service.ServiceEntity;
import backend.model.service.ServicePersistenceUnit;
import backend.model.service.ServiceRepository;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class GenericServiceProvider implements ExtensionPoint, ServiceProvider{
	
	public static class ServiceProviderDescriptor extends Descriptor<GenericServiceProvider>
	{
		public ServiceProviderDescriptor(Class<GenericServiceProvider> providerClass)
		{
			super(providerClass);
		}
	}
	
	@JsonProperty("descriptor")
    private ServiceProviderDescriptor m_descriptor;
    
	@JsonProperty("services")
    private HashMap<String, ServiceEntity.ServiceDescriptor> m_registeredServices;
    
    private GlobalPersistenceUnit m_globalPersistenceUnit;
    private ServicePersistenceUnit m_servicePersistenceUnit;
    JobExecutor m_jobExecutor;
    
    public GenericServiceProvider()
    {
	    	m_descriptor = new ServiceProviderDescriptor((Class<GenericServiceProvider>)this.getClass());
	    	m_descriptor.commonName(this.getClass().getName());
	    	m_registeredServices = new HashMap<String, ServiceEntity.ServiceDescriptor>();
	    	m_jobExecutor = new JobExecutor();
	    	registerServices();
    }
    
    @JsonProperty("descriptor")
    public ServiceProviderDescriptor descriptor()
    {
    	return m_descriptor;
    }
    
    public String commonName()
    {
    	return m_descriptor.commonName();
    }
    
    protected void commonName(String name)
    {
    	m_descriptor.commonName(name);
    }
    
    @Override
    public ServiceEntity.ServiceDescriptor serviceDescriptor(String serviceIdentifier)
    {
    	return m_registeredServices.get(serviceIdentifier);
    }
    
    @JsonProperty("services")
    @Override
    public HashMap<String, ServiceEntity.ServiceDescriptor> services()
    {
    	return m_registeredServices;
    }
    
    @Override
    public <E extends Result> ServiceEntity<E> service(String serviceIdentifier) throws InstantiationException, IllegalAccessException
    {
    	Class<ServiceEntity> serviceClass = m_registeredServices.get(serviceIdentifier).classDescriptor();
    	ServiceEntity<E> newService = (ServiceEntity<E>) serviceClass.newInstance();
    	
    	newService.jobExecutor(m_jobExecutor);
    	
    	if(m_globalPersistenceUnit != null)
    		newService.persistenceUnit(m_globalPersistenceUnit);
    	
    	if(m_servicePersistenceUnit != null)
    		m_servicePersistenceUnit.save(newService);
    	
    	newService.providerIdentifier(m_descriptor.identifier());
    	
    	return newService;
    }
    
    @Override
    public <T extends Result> T executeService(Service<T> serviceToExecute)
    {
    	serviceToExecute.execute();
    	return serviceToExecute.result();
    }
    
    private void registerServices()
    {    	
    	// create scanner and disable default filters (that is the 'false' argument)
    	final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
    	ClassLoader classLoader = this.getClass().getClassLoader();
    	// set current classes classloader in case it had been loaded as a plugin
    	provider.setResourceLoader(new PathMatchingResourcePatternResolver(classLoader));
    	// add include filters which matches all the classes (or use your own)
    	provider.addIncludeFilter((TypeFilter) new AssignableTypeFilter(ServiceEntity.class));
    	// get matching classes defined in the package
    	final Set<BeanDefinition> classes = provider.findCandidateComponents(this.getClass().getPackage().getName());
    	
    	for (BeanDefinition definition : classes) {
			try 
			{
				Class<ServiceEntity> registeredClass;
				registeredClass = (Class<ServiceEntity>) classLoader.loadClass(definition.getBeanClassName());

				ServiceEntity instance = registeredClass.newInstance();
				String commonName = instance.commonName();
				
				instance.providerIdentifier(m_descriptor.identifier());
				
				m_registeredServices.put(instance.descriptor().identifier(), instance.descriptor());
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
			catch (InstantiationException e) 
			{
				e.printStackTrace();
			}
    	}
    }
    
	@Override
	public void persistenceUnit(GlobalPersistenceUnit persistenceUnit) {
		m_globalPersistenceUnit = persistenceUnit;
		m_servicePersistenceUnit = persistenceUnit.servicePersistence();
	}
	
	@Override
	public GlobalPersistenceUnit persistenceUnit()
	{
		return m_globalPersistenceUnit;
	}
    
}
