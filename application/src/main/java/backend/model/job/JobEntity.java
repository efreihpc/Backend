package backend.model.job;

import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import backend.model.result.Result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Inheritance

//T specifies the jobs result type
public abstract class JobEntity<T extends Result> implements Job {
	
	public class JobDescriptor
	{
		private Class<JobEntity> m_classDescriptor;
		
		@JsonProperty("commonName")
		private String m_commonName;
		
		public JobDescriptor(Class<JobEntity> clazz)
		{
			m_classDescriptor = clazz;
		}
		
		public Class<JobEntity> classDescriptor()
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
	
	@JsonIgnore
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long m_id;
	
    @JsonProperty("descriptor")
    @Transient
    private JobDescriptor m_descriptor;
    
    @JsonProperty("result")
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Result.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private T m_result;

    @JsonProperty("secondaryJobs")
    @OneToMany(fetch = FetchType.EAGER)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<JobEntity> m_secondaryJobs = new Vector<JobEntity>();
	
    @Transient
    private JobExecutor m_executor;
    
    public JobEntity()
    {
    	m_descriptor = new JobDescriptor((Class<JobEntity>)this.getClass());
    	commonName(this.getClass().getName());
    }
    
    @JsonProperty("descriptor")
    public JobDescriptor descriptor()
    {
    	return m_descriptor;
    }
    
    @JsonIgnore
    public long id()
    {
    	return m_id;
    }
    
    public void commonName(String name)
    {
    	m_descriptor.commonName(name);
    }
    
    protected String commonName()
    {
    	return m_descriptor.commonName();
    }
    
    @JsonProperty("result")
    public T result()
    {
    	return m_result;
    }
    
    @JsonProperty("result")
    protected void result(T result)
    {
    	m_result = result;
    } 
    
    @JsonProperty("secondaryJobs")
    public void addSecondaryJob(JobEntity job)
    {
    	m_secondaryJobs.add(job);
    }
    
    @JsonProperty("secondaryJobs")
    public List<JobEntity> secondaryJobs()
    {
    	return m_secondaryJobs;
    }
    
    public void executor(JobExecutor executor)
    {
    	m_executor = executor;
    	for (JobEntity job : m_secondaryJobs) {
    		job.executor(executor);
    	}
    }
    
    public JobExecutor executor()
    {
    	return m_executor;
    }
    
    public JobExecutor taskExecutor()
    {
    	return m_executor;
    }
    
    private void runSecondaryJobs()
    {
    	if(m_executor != null)
	    	for(JobEntity job : m_secondaryJobs)
	    	{
	    		m_executor.execute(job);
	    	}
    }
    
	public final void run()
	{
		execute();
		runSecondaryJobs();
	}
	
	protected abstract void execute();
}
