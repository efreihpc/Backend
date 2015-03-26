package backend.model.task;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

import backend.model.descriptor.Describable;
import backend.model.result.Result;
import backend.system.Configurable;

@Entity
@Inheritance
public abstract class Task <T extends Result> implements Runnable, Describable, Configurable{
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long m_id;
    
    @JsonProperty("configuration")
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Result.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Result m_configuration;
	
	public abstract T result();
	public abstract void execute();
	
	public void run()
	{
		execute();
	}
	
	public long id()
	{
		return m_id;
	}
	
    @JsonProperty("configuration")
    public Result configuration()
    {
    	return m_configuration;
    }
    
    @JsonProperty("configuration")
    public void configuration(Result configuration)
    {
    	m_configuration = configuration;
    }  
}
