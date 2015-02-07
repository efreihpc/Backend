package backend.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import backend.system.ServicePersistenceUnit;

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
    private HashMap<String, Class<GenericService>> m_registeredServices;
    @Transient
    private ServicePersistenceUnit m_servicePersistenceUnit;
    
    public GenericServiceProvider()
    {
    	registerServices();
    }
    
    public static String name()
    {
    	return m_name;
    }
    
    @Override
    public List<String> services()
    {
    	return new ArrayList<String>(m_registeredServices.keySet());
    }
    
    @Override
    public <E> GenericService<E> service(String serviceName) throws InstantiationException, IllegalAccessException
    {
    	Class<GenericService> serviceClass = m_registeredServices.get(serviceName);
    	GenericService<E> newService = (GenericService<E>) serviceClass.newInstance();
    	m_servicePersistenceUnit.serviceRepository().save(newService);
    	
    	return newService;
    }
    
    @Override
    public <T> T executeService(Service<T> serviceToExecute)
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

				Method method = registeredClass.getMethod("name");
				String classname = (String) method.invoke(null);
    		
				m_registeredServices.put(classname, registeredClass);
			} 
			catch (NoSuchMethodException | SecurityException e) 
			{
				e.printStackTrace();
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
			catch (InvocationTargetException e) 
			{
				e.printStackTrace();
			}
    	}
    }
    
	@Override
	public void persistenceUnit(ServicePersistenceUnit persistenceUnit) {
		m_servicePersistenceUnit = persistenceUnit;
	}
	
	@Override
	public ServicePersistenceUnit persistenceUnit()
	{
		return m_servicePersistenceUnit;
	}
    
}
