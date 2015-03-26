package backend.model.dependency;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;

import backend.model.descriptor.ServiceDescriptor;
import backend.model.result.Result;
import backend.model.service.ServiceEntity;

@Entity
@Inheritance
public class ServiceDependency<T extends Result> extends Dependency<ServiceEntity<T>, T>{
	
    @OneToOne(fetch = FetchType.EAGER, targetEntity = ServiceDescriptor.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	ServiceDescriptor m_serviceDescriptor;
    
    public ServiceDependency()
    {
    }
    
    public static ServiceDependency create(ServiceDescriptor descriptor, Result configuration)
    {
    	ServiceDependency newDep = new ServiceDependency();
    	newDep.descriptor(descriptor);
    	newDep.configuration(configuration);
    	return newDep;
    }
	
	public void descriptor(ServiceDescriptor descriptor)
	{
		m_serviceDescriptor = descriptor;
	}
	
	public ServiceDescriptor descriptor()
	{
		return m_serviceDescriptor;
	}
}
