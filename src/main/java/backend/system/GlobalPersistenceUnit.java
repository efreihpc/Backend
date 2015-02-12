package backend.system;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import backend.model.job.JobRepository;
import backend.model.service.ServiceRepository;
import backend.model.serviceprovider.ServiceProviderRepository;

public class GlobalPersistenceUnit implements
	ServiceProviderPersistenceUnit,
	ServicePersistenceUnit,
	JobPersistenceUnit{
	
	private ApplicationContext m_context;
	
	ServiceProviderRepository m_serviceProviderRepository;
	ServiceRepository m_serviceRepository;
	JobRepository m_jobRepository;
	
	public GlobalPersistenceUnit()
	{
		m_context = new ClassPathXmlApplicationContext("Spring-Config.xml");
		m_serviceProviderRepository = m_context.getBean(ServiceProviderRepository.class);
		m_serviceRepository = m_context.getBean(ServiceRepository.class);
	    m_jobRepository = m_context.getBean(JobRepository.class);
	}

	@Override
	public JobRepository jobRepository() {
		return m_jobRepository;
	}

	@Override
	public ServiceRepository serviceRepository() {
		return m_serviceRepository;
	}

	@Override
	public ServiceProviderRepository serviceProviderRepository() {
		return m_serviceProviderRepository;
	}

}
