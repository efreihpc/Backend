package backend.model.serviceprovider;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import backend.model.result.Result;
import backend.model.service.GenericService;
import backend.model.service.Service;
import backend.model.service.ServiceRepository;
import backend.system.GlobalPersistenceUnit;
import backend.system.JobExecutor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Inheritance
@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "type")  
public abstract class GenericServiceProvider implements ServiceProvider{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long m_id;
    private static String m_name;
    
    @Transient
    private HashMap<String, GenericService.ServiceDescriptor> m_registeredServices;
    @Transient
    private GlobalPersistenceUnit m_globalPersistenceUnit;
    @Transient
    private ServiceRepository m_serviceRepository;
    @Transient
    JobExecutor m_jobExecutor;
    
    public GenericServiceProvider()
    {
    	name(this.getClass().getName());
    	m_registeredServices = new HashMap<String, GenericService.ServiceDescriptor>();
    	m_jobExecutor = new JobExecutor();
    	registerServices();
    }
    
    public static String name()
    {
    	return m_name;
    }
    
    protected static void name(String name)
    {
    	m_name = name;
    }
    
    @Override
    public GenericService.ServiceDescriptor serviceDescriptor(String serviceIdentifier)
    {
    	return m_registeredServices.get(serviceIdentifier);
    }
    
    @Override
    public HashMap<String, GenericService.ServiceDescriptor> services()
    {
    	return m_registeredServices;
    }
    
    @Override
    public <E extends Result> GenericService<E> service(String serviceName) throws InstantiationException, IllegalAccessException
    {
    	Class<GenericService> serviceClass = m_registeredServices.get(serviceName).classDescriptor();
    	GenericService<E> newService = (GenericService<E>) serviceClass.newInstance();
    	
    	newService.jobExecutor(m_jobExecutor);
    	
    	if(m_globalPersistenceUnit != null)
    		newService.persistenceUnit(m_globalPersistenceUnit);
    	
    	if(m_serviceRepository != null)
    		m_serviceRepository.save(newService);
    	
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
    	// add include filters which matches all the classes (or use your own)
    	provider.addIncludeFilter((TypeFilter) new AssignableTypeFilter(GenericService.class));
    	// get matching classes defined in the package
    	final Set<BeanDefinition> classes = provider.findCandidateComponents(this.getClass().getPackage().getName());
    	
    	for (BeanDefinition definition : classes) {
			try 
			{
				Class<GenericService> registeredClass;
				registeredClass = (Class<GenericService>) Class.forName(definition.getBeanClassName());

				GenericService instance = registeredClass.newInstance();
				String commonName = instance.commonName();
				String identifier = registeredClass.getCanonicalName();
				MessageDigest messageDigest = MessageDigest.getInstance("SHA");
				messageDigest.update(identifier.getBytes());
				identifier = String.format("%040x", new BigInteger(1, messageDigest.digest()));
				
				m_registeredServices.put(identifier, instance.descriptor());
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
    
	@Override
	public void persistenceUnit(GlobalPersistenceUnit persistenceUnit) {
		m_globalPersistenceUnit = persistenceUnit;
		m_serviceRepository = persistenceUnit.serviceRepository();
	}
	
	@Override
	public GlobalPersistenceUnit persistenceUnit()
	{
		return m_globalPersistenceUnit;
	}
    
}
