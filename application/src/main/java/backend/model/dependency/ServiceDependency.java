package backend.model.dependency;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import backend.model.Descriptor;
import backend.model.result.Result;
import backend.model.service.ServiceEntity;

@Entity
@Inheritance

public class ServiceDependency<T extends Result> extends Dependency<ServiceEntity<T>, T>{
	
	ServiceEntity.ServiceDescriptor m_serviceDescriptor;
	
	public ServiceDependency(ServiceEntity.ServiceDescriptor descriptor)
	{
		m_serviceDescriptor = descriptor;
	}
	
	public ServiceEntity.ServiceDescriptor descriptor()
	{
		return m_serviceDescriptor;
	}
}
