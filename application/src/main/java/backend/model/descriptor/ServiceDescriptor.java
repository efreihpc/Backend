package backend.model.descriptor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import backend.model.service.ServiceEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Inheritance
public class ServiceDescriptor extends Descriptor<ServiceEntity>
{
	@JsonProperty("providerIdentifier")
	private String m_providerIdentifier;
	
	public ServiceDescriptor()
	{
		super();	
	}
	
	public ServiceDescriptor(Class<ServiceEntity> clazz)
	{
		super(clazz);
	}
	
	@JsonProperty("providerIdentifier")
	public void providerIdentifier(String name)
	{
		m_providerIdentifier = name;
	}
	
	@JsonProperty("providerIdentifier")
	public String providerIdentifier()
	{
		return m_providerIdentifier;
	}
}