package backend.model;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.core.task.TaskExecutor;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Inheritance
@JsonTypeInfo(  
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "type")  
//@JsonSubTypes({  
//    @Type(value = Prototype.class, name = "Prototype") })

//T specifies the jobs result type
public abstract class GenericJob<T> implements Job {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long m_id;
    private String m_name;
    private Result<T> m_result;

	@OneToMany
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<GenericJob> m_secondaryJobs = new Vector<GenericJob>();
	
    @Transient
    private TaskExecutor m_taskExecutor;
    
    public GenericJob()
    {
    	
    }
        
    public GenericJob(TaskExecutor executor)
    {
    	m_taskExecutor = executor;
    }
    
    public long getId()
    {
    	return m_id;
    }
    
    public void setName(String name)
    {
    	m_name = name;
    }
    
    public String getName()
    {
    	return m_name;
    }
    
    public Result<T> getResult()
    {
    	return m_result;
    }
    
    void setResults(Result<T> result)
    {
    	m_result = result;
    }
    
    protected void addResult(String key, T value)
    {
    	m_result.addValue(key, value);
    }
    
    public void addSecondaryJob(GenericJob job)
    {
    	m_secondaryJobs.add(job);
    }
    
    public void setTaskExecutor(TaskExecutor executor)
    {
    	m_taskExecutor = executor;
    	for (GenericJob job : m_secondaryJobs) {
    		job.setTaskExecutor(executor);
    	}
    }
    
    public TaskExecutor getTaskExecutor()
    {
    	return m_taskExecutor;
    }
    
    private void runSecondaryJobs()
    {
    	if(m_taskExecutor != null)
	    	for(GenericJob job : m_secondaryJobs)
	    	{
	    		m_taskExecutor.execute(job);
	    	}
    }
    
	public final void run()
	{
		execute();
		runSecondaryJobs();
	}
	
	protected abstract void execute();
}
