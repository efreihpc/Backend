package backend.model.task;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import backend.model.descriptor.Describable;
import backend.model.result.Result;
import backend.model.result.ResultRepository;
import backend.system.Configurable;
import backend.system.GlobalPersistenceUnit;
import backend.system.GlobalState;

//TODO: check if mandatory configuration is given
@Entity
@Inheritance
public abstract class Task <T extends Result> implements Runnable, Describable, Configurable{
	
	@JsonProperty("Id")
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long m_id;
    
    @JsonProperty("configuration")
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Result.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.MERGE)
    private Result m_configuration;
    
    @Transient protected ResultRepository m_resultRepository;
	
	public abstract T result();
	public abstract void execute();
	
	public Task()
	{
	}
	
	public void run()
	{
		execute();
	}
	
	@JsonProperty("Id")
	public long id()
	{
		return m_id;
	}
	
	@JsonProperty("Id")
	public void id(long id)
	{
		m_id = id;
	}
	
	protected abstract void configured();
	
	
    @JsonProperty("configuration")
    public Result configuration()
    {
    	return m_configuration;
    }
    
    @JsonProperty("configuration")
    public void configuration(Result configuration)
    {
    	m_configuration = configuration;
    	configured();
    	
    	if(m_resultRepository == null)
    	{
    		GlobalPersistenceUnit globalPersistence = GlobalState.get("GlobalPersistenceUnit");
    		m_resultRepository = globalPersistence.resultRepository();	
    	}
    	
    	if(configuration != null)
    		m_resultRepository.save(configuration);
    }  
    
    public void resultRepository(ResultRepository resultRepository)
    {
    	m_resultRepository = resultRepository;
    }
    
    public ResultRepository resultRepository()
    {
    	return m_resultRepository;
    }
}
