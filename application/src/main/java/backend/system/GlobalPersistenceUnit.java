package backend.system;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import backend.model.job.JobPersistenceUnit;
import backend.model.result.ResultPersistenceUnit;
import backend.model.result.ResultRepository;
import backend.model.service.ServicePersistenceUnit;
import backend.model.serviceprovider.ServiceProviderPersistenceUnit;
import backend.model.serviceprovider.ServiceProviderRepository;
import backend.model.task.TaskRepository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class GlobalPersistenceUnit implements
	ServiceProviderPersistenceUnit,
	ResultPersistenceUnit,
	MongoPersistenceUnit{
	
	private ApplicationContext m_context;
	
	ServiceProviderRepository m_serviceProviderRepository;
	ServicePersistenceUnit m_servicePersistence;
	JobPersistenceUnit m_jobPersistence;
	ResultRepository m_resultRepository;
	TaskRepository m_taskRepository;
	
	MongoClient m_mongoClient;
	MongoDatabase m_mongoDb;
	
	public GlobalPersistenceUnit()
	{
		m_context = new ClassPathXmlApplicationContext("Spring-Config.xml");
		m_serviceProviderRepository = new ServiceProviderRepository();
		m_servicePersistence = new ServicePersistenceUnit();
		m_jobPersistence = new JobPersistenceUnit();
	    m_resultRepository = m_context.getBean(ResultRepository.class);  
	    m_taskRepository = m_context.getBean(TaskRepository.class);  
	    
		m_mongoClient = new MongoClient( "localhost" , 27017 );
		m_mongoDb = m_mongoClient.getDatabase("test");
	}
	
	protected void finalize()
	{
		m_mongoClient.close();
	}

	@Override
	public ServiceProviderRepository serviceProviderRepository() {
		return m_serviceProviderRepository;
	}
	
	@Override
	public ResultRepository resultRepository() {
		return m_resultRepository;
	}
	
	public TaskRepository taskRepository() {
		return m_taskRepository;
	}
	
	public ServicePersistenceUnit servicePersistence()
	{
		return m_servicePersistence;
	}
	
	public JobPersistenceUnit jobPersistence()
	{
		return m_jobPersistence;
	}

	@Override
	public MongoDatabase getMongoDb() {
		return m_mongoDb;
	}
}
