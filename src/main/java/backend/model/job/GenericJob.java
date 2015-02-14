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

import org.hibernate.annotations.*;

import backend.model.result.Result;

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
public abstract class GenericJob<T extends Result> implements Job {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long m_id;
    private static String s_name;
    
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Result.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private T m_result;

	@OneToMany
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<GenericJob> m_secondaryJobs = new Vector<GenericJob>();
	
    @Transient
    private JobExecutor m_executor;
    
    public GenericJob()
    {
    	name(this.getClass().getName());
    }
    
    public long getId()
    {
    	return m_id;
    }
    
    public static void name(String name)
    {
    	s_name = name;
    }
    
    protected static String name()
    {
    	return s_name;
    }
    
    public T result()
    {
    	return m_result;
    }
    
    protected void result(T result)
    {
    	m_result = result;
    } 
    
    public void addSecondaryJob(GenericJob job)
    {
    	m_secondaryJobs.add(job);
    }
    
    public void executor(JobExecutor executor)
    {
    	m_executor = executor;
    	for (GenericJob job : m_secondaryJobs) {
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
	    	for(GenericJob job : m_secondaryJobs)
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
