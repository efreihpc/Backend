package frontend;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import backend.model.descriptor.ServiceDescriptor;

public class ServiceTransfer {
	@JsonProperty("descriptor")
	ServiceDescriptor m_descriptor;
	
	@JsonProperty("configuration")
	HashMap<String, String> m_configuration;

	public ServiceDescriptor descriptor() {
		return m_descriptor;
	}

	public void descriptor(ServiceDescriptor m_descriptor) {
		this.m_descriptor = m_descriptor;
	}

	public HashMap<String, String> configuration() {
		return m_configuration;
	}

	public void configuration(HashMap<String, String> m_configuration) {
		this.m_configuration = m_configuration;
	}
}
