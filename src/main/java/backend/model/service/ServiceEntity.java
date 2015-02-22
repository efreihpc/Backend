package backend.model.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.transaction.annotation.Transactional;

import ro.fortsoft.pf4j.ExtensionPoint;
import backend.model.Descriptor;
import backend.model.GlobalPersistenceUnit;
import backend.model.job.JobEntity;
import backend.model.job.JobExecutor;
import backend.model.job.JobRepository;
import backend.model.job.PersistJob;
import backend.model.result.Result;
import backend.model.serviceprovider.ServiceProviderRepository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Inheritance
public abstract class ServiceEntity<T extends Result> implements ExtensionPoint, Service<T> {
	
	public static class ServiceDescriptor extends Descriptor<ServiceEntity>
	{
		@JsonProperty("providerIdentifier")
		private String m_providerIdentifier;
		
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
		m_jobRepository = persistenceUnit.jobRepository();
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
