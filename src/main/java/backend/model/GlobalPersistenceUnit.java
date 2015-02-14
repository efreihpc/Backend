package backend.model;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import backend.model.job.JobPersistenceUnit;
import backend.model.job.JobRepository;
import backend.model.result.ResultPersistenceUnit;
import backend.model.result.ResultRepository;
import backend.model.service.ServicePersistenceUnit;
import backend.model.service.ServiceRepository;
import backend.model.serviceprovider.ServiceProviderPersistenceUnit;
import backend.model.serviceprovider.ServiceProviderRepository;

public class GlobalPersistenceUnit implements
	ServiceProviderPersistenceUnit,
	ServicePersistenceUnit,
	JobPersistenceUnit,
	ResultPersistenceUnit{
	
	private ApplicationContext m_context;
	
	ServiceProviderRepository m_serviceProviderRepository;
	ServiceRepository m_serviceRepository;
	JobRepository m_jobRepository;
	ResultRepository m_resultRepository;
	
	public GlobalPersistenceUnit()
	{
		m_context = new ClassPathXmlApplicationContext("Spring-Config.xml");
		m_serviceProviderRepository = new ServiceProviderRepository();
		m_serviceRepository = m_context.getBean(ServiceRepository.class);
	    m_jobRepository = m_context.getBean(JobRepository.class);
	    m_resultRepository = m_context.getBean(ResultRepository.class);
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
	
	@Override
	public ResultRepository resultRepository() {
		return m_resultRepository;
	}

}
