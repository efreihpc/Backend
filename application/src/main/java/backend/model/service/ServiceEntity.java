package backend.model.service;

import static reactor.event.selector.Selectors.$;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import reactor.core.Reactor;
import reactor.event.Event;
import reactor.function.Consumer;
import backend.model.dependency.ServiceDependency;
import backend.model.descriptor.ServiceDescriptor;
import backend.model.job.JobEntity;
import backend.model.job.JobPersistenceUnit;
import backend.model.job.PersistJob;
import backend.model.result.Result;
import backend.model.serviceprovider.ServiceProviderRepository;
import backend.model.task.ConfigurationFailedException;
import backend.system.GlobalPersistenceUnit;
import backend.system.GlobalState;
import backend.system.execution.ThreadPoolExecutor;

import com.fasterxml.jackson.annotation.JsonProperty;


//TODO: Service should not persist himself
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@org.springframework.stereotype.Service
@org.hibernate.annotations.DiscriminatorOptions(force=true)
public abstract class ServiceEntity<T extends Result> extends Service<T> implements Consumer<Event<Long>>
{    
    @JsonProperty("descriptor")
    @Transient
    private ServiceDescriptor m_descriptor;
         
    @JsonProperty("result")
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Result.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.MERGE)
    private T m_result;
    
	protected String m_classLoader;
	
    @OneToMany(targetEntity=ServiceDependency.class, fetch = FetchType.EAGER)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	protected List<ServiceDependency> m_dependencies;
    
    @Transient
    private GlobalPersistenceUnit m_globalPersistenceUnit;
    @Transient
    private JobPersistenceUnit m_jobPersistence;
    @Transient
    private ServicePersistenceUnit m_servicePersistence;
    @Transient 
    private ThreadPoolExecutor m_jobExecutor;
    @Transient
    private HashSet<Long> m_waitingForJobs;
    @Transient
    Reactor m_reactor;
    
    
    public ServiceEntity()
    {
    	m_reactor = GlobalState.get("eventReactor");
    	m_dependencies = new Vector<ServiceDependency>();
    	m_descriptor = new ServiceDescriptor((Class<ServiceEntity>)this.getClass());
    	m_descriptor.commonName(this.getClass().getName());
    	m_waitingForJobs = new HashSet<Long>();
    	m_classLoader = "Default";
    }    
    
    @JsonProperty("descriptor")
    public ServiceDescriptor descriptor()
    {
    	return m_descriptor;
    }
    
    public String commonName()
    {
    	return m_descriptor.commonName();
    }
    
    protected void commonName(String name)
    {
    	m_descriptor.commonName(name);
    }
    
    public void providerIdentifier(String providerIdentifier)
    {
    	m_descriptor.providerIdentifier(providerIdentifier);
    }
    
    public String providerIdentifier()
    {
    	return m_descriptor.providerIdentifier();
    }
    
    @JsonProperty("result")
    protected void result(T result)
    {
    	m_result = result;
    }
    
    @JsonProperty("result")
    public T result()
    {
    	return m_result;
    }
    
	@Override
	public void persistenceUnit(GlobalPersistenceUnit persistenceUnit) {
		m_globalPersistenceUnit = persistenceUnit;
		m_jobPersistence = persistenceUnit.jobPersistence();
		m_servicePersistence = persistenceUnit.servicePersistence();
	}
	
	@Override
	public GlobalPersistenceUnit persistenceUnit()
	{
		return m_globalPersistenceUnit;
	}
	
    protected ServiceProviderRepository serviceProviderRepository()
    {
    	return m_globalPersistenceUnit.serviceProviderRepository();
    }
	
	@JsonProperty("dependencies")
	public List<ServiceDependency> dependencies()
	{
		return m_dependencies;
	}
	
	protected void addDependency(String serviceIdentifier, String serviceProviderIdentifier)
	{
		addDependency(serviceIdentifier, serviceProviderIdentifier, configuration());
	}
	
	protected void addDependency(String serviceIdentifier, String serviceProviderIdentifier, Result configuration)
	{
		ServiceDescriptor descriptor = new ServiceDescriptor();
		descriptor.identifier(serviceIdentifier);
		descriptor.providerIdentifier(serviceProviderIdentifier);
		addDependency(descriptor, configuration);
	}
	
	protected void addDependency(ServiceDescriptor descriptor, Result configuration)
	{
		m_dependencies.add(ServiceDependency.create(descriptor, configuration));
	}
	
	@Override
	public void jobExecutor(ThreadPoolExecutor jobExecutor)
	{
		m_jobExecutor = jobExecutor;
	}
	
	@Override
	public ThreadPoolExecutor jobExecutor()
	{
		return m_jobExecutor;
	}
	
	protected void executeJob(JobEntity job)
	{		
		job.executor(m_jobExecutor);
		job.resultRepository(m_globalPersistenceUnit.resultRepository());
		
		if(job.configuration() == null)
			try 
			{
				job.configuration(configuration());
			} 
			catch (ConfigurationFailedException e) 
			{
				System.out.println("ServiceEntity> Configuration Error: ");
				System.out.println("\t" + e.getMessage());
				e.printStackTrace();
			}
		
		PersistJob persist = new PersistJob();
		persist.jobRepository(m_jobPersistence);
		persist.jobToPersist(job);
		job.addSecondaryJob(persist);
		
		if(m_jobPersistence != null)
			m_jobPersistence.save(job);
		
		m_waitingForJobs.add(job.id());
		System.out.println(commonName() + ":" + id() +"> registering notification from: " + "job_finish" + job.id());
		m_reactor.on($("job_finish" + job.id()), this);
		m_jobExecutor.execute(job);
	}
	
    public void accept(Event<Long> ev) {
		m_waitingForJobs.remove(ev.getData());
    	System.out.println(commonName() + ":" + id() +"> recieved job_finish" + ev.getData());
    	System.out.println(commonName() + ":" + id() +"> still waiting for " + m_waitingForJobs.size() + "jobs");
		
		if(m_waitingForJobs.size() == 0)
		{
			System.out.println("ServiceEntity> Notifying Service finished: " + descriptor().commonName() + id());
			m_reactor.notify("service_finish" + id(), Event.wrap(id()));
			finished();
			System.out.println("ServiceEntity> Persisting: " + commonName() + ":" + id());
			m_servicePersistence.save(this);
		}

    }
    
    protected void finished()
    {
    }
}
