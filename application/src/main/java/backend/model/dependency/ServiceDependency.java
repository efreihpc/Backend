package backend.model.dependency;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import backend.model.descriptor.ServiceDescriptor;
import backend.model.result.Result;
import backend.model.service.ServiceEntity;

@Entity
@Inheritance
public class ServiceDependency<T extends Result> extends Dependency<ServiceEntity<T>, T>{
	
    @OneToOne(fetch = FetchType.EAGER, targetEntity = ServiceDescriptor.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	ServiceDescriptor m_serviceDescriptor;
	
	public ServiceDependency(ServiceDescriptor descriptor)
	{
		m_serviceDescriptor = descriptor;
	}
	
	public ServiceDescriptor descriptor()
	{
		return m_serviceDescriptor;
	}
}