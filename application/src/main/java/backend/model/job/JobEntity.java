package backend.model.job;

import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import reactor.core.Reactor;
import reactor.event.Event;
import backend.model.descriptor.Descriptor;
import backend.model.result.DictionaryResult;
import backend.model.result.Result;
import backend.model.result.ResultRepository;
import backend.system.GlobalState;
import backend.system.execution.ThreadPoolExecutor;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Inheritance
@org.springframework.stereotype.Service
//T specifies the jobs result type
public abstract class JobEntity<T extends Result> extends Job<T>
{

	protected String m_classLoader;
	
    @JsonProperty("descriptor")
    @Transient
    private Descriptor m_descriptor;
    
    @JsonProperty("result")
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Result.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private T m_result;

    @JsonProperty("secondaryJobs")
    @OneToMany(fetch = FetchType.EAGER)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<JobEntity> m_secondaryJobs = new Vector<JobEntity>();
    @Transient
    Reactor m_reactor;
	
    @Transient
    private ThreadPoolExecutor m_executor;
    
    @Transient
    private ResultRepository m_resultRepository;
    
    public JobEntity()
    {
    	m_reactor = GlobalState.get("eventReactor");
    	m_descriptor = new Descriptor((Class<JobEntity>)this.getClass());
    	commonName(this.getClass().getName());
    }
    
    @JsonProperty("descriptor")
    public Descriptor descriptor()
    {
    	return m_descriptor;
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
    
    public void resultRepository(ResultRepository resultRepository)
    {
    	m_resultRepository = resultRepository;
    }
    
    public void executor(ThreadPoolExecutor executor)
    {
    	m_executor = executor;
    	for (JobEntity job : m_secondaryJobs) {
    		job.executor(executor);
    	}
    }
    
    public ThreadPoolExecutor executor()
    {
    	return m_executor;
    }
    
    public ThreadPoolExecutor taskExecutor()
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
		if(m_result != null)
			m_resultRepository.save(result());
		System.out.println("JobEntity> Notifying job finish: " + descriptor().commonName() + id());
		m_reactor.notify("job_finish" + id(), Event.wrap(id()));
	}
}
