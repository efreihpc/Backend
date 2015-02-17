package backend.model.service;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import ro.fortsoft.pf4j.ExtensionPoint;
import backend.model.GlobalPersistenceUnit;
import backend.model.job.JobEntity;
import backend.model.job.JobExecutor;
import backend.model.job.JobRepository;
import backend.model.job.PersistJob;
import backend.model.result.Result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Inheritance
public abstract class ServiceEntity<T extends Result> implements ExtensionPoint, Service<T> {
	
	public static class ServiceDescriptor
	{
		private Class<ServiceEntity> m_classDescriptor;
		
		@JsonProperty("commonName")
		private String m_commonName;
		
		public ServiceDescriptor(Class<ServiceEntity> clazz)
		{
			m_classDescriptor = clazz;
		}
		
		public Class<ServiceEntity> classDescriptor()
		{
			return m_classDescriptor;
		}	
		
		@JsonProperty("commonName")
		public void commonName(String name)
		{
			m_commonName = name;
		}
		
		@JsonProperty("commonName")
		public String commonName()
		{
			return m_commonName;
		}
	}

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long m_id;
    
    @JsonProperty("descriptor")
    @Transient
    private ServiceDescriptor m_descriptor;
       
    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, targetEntity = ServiceEntity.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private ServiceEntity<T> m_dataSource;
    
    @JsonProperty("result")
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Result.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private T m_result;
    
    @Transient
    private GlobalPersistenceUnit m_globalPersistenceUnit;
    @Transient
    private JobRepository m_jobRepository;
    @Transient 
    private JobExecutor m_jobExecutor;
    
    public ServiceEntity()
    {
    	m_descriptor = new ServiceDescriptor((Class<ServiceEntity>)this.getClass());
    	m_descriptor.commonName(this.getClass().getName());
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
		m_jobRepository = persistenceUnit.jobRepository();
	}
	
	@Override
	public GlobalPersistenceUnit persistenceUnit()
	{
		return m_globalPersistenceUnit;
	}
	
	@JsonProperty("dataSource")
	public void dataSource(ServiceEntity<T> dataSource)
	{
		m_dataSource = dataSource;
	}
	
	@JsonProperty("dataSource")
	public ServiceEntity<T> dataSource()
	{
		return m_dataSource;
	}
	
	protected T data()
	{
		return m_dataSource.result();
	}
	
	@Override
	public void jobExecutor(JobExecutor jobExecutor)
	{
		m_jobExecutor = jobExecutor;
	}
	
	@Override
	public JobExecutor jobExecutor()
	{
		return m_jobExecutor;
	}
	
	protected void executeJob(JobEntity job)
	{
		job.executor(m_jobExecutor);
		
		PersistJob persist = new PersistJob();
		persist.jobRepository(m_jobRepository);
		persist.jobToPersist(job);
		job.addSecondaryJob(persist);
		
		if(m_jobRepository != null)
			m_jobRepository.save(job);
		
		m_jobExecutor.execute(job);
	}
}
