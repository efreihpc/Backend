package backend.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import backend.model.job.PersistJob;
import backend.system.GlobalPersistenceUnit;
import backend.system.JobExecutor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Inheritance
@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "type")  
public abstract class GenericService<T extends Result> implements Service<T> {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long m_id;
    private static String m_name;
    
    @OneToOne(fetch = FetchType.EAGER, targetEntity = GenericService.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private GenericService<T> m_dataSource;
    
    @Transient
    private GlobalPersistenceUnit m_globalPersistenceUnit;
    @Transient
    private JobRepository m_jobRepository;
    @Transient 
    private JobExecutor m_jobExecutor;
    
    public GenericService()
    {
    	name(this.getClass().getName());
    }
    
    public static String name()
    {
    	return m_name;
    }
    
    protected static void name(String name)
    {
    	m_name = name;
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
	
	public void dataSource(GenericService<T> dataSource)
	{
		m_dataSource = dataSource;
	}
	
	public GenericService<T> dataSource()
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
	
	protected void executeJob(GenericJob job)
	{
		job.executor(m_jobExecutor);
		
		PersistJob persist = new PersistJob();
		persist.jobRepository(m_jobRepository);
		persist.jobToPersist(job);
		job.addSecondaryJob(persist);
		
		m_jobRepository.save(job);
		m_jobExecutor.execute(job);
	}
	
	public Iterable<GenericJob> getAllJobs()
	{
		return m_jobRepository.findAll();
	}
}
