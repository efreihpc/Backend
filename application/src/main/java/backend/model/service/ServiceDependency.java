package backend.model.service;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import backend.model.Descriptor;
import backend.model.dependency.Dependency;
import backend.model.result.Result;

@Entity
@Inheritance

public class ServiceDependency<T extends Result> implements Dependency<ServiceEntity<T>, T>{
	
	Descriptor<ServiceEntity<T>> m_serviceDescriptor;
	
	@Transient
	


	@Override
	public T result() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Descriptor<ServiceEntity<T>> descriptor() {
		return m_serviceDescriptor;
	}

}
