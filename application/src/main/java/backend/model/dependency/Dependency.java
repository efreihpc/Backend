package backend.model.dependency;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import backend.model.result.Result;
import backend.model.task.Task;

@Entity
@Inheritance
public abstract class Dependency<T extends Task<U>, U extends Result>  implements DependencyInterface<T, U> {
		
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long m_id;
	
	@Transient
	protected T m_task;
	
    @JsonProperty("configuration")
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Result.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.MERGE)
	private Result m_configuration;
	
	@Override
	public void task(T task)
	{
		m_task = task;
		if(m_configuration != null)
			m_task.configuration(m_configuration);
	}

	@Override
	public U result() {
		return m_task.result();
	}
	
	@Override 
	public boolean satisfied()
	{
		return m_task != null;
	}
	
	public void configuration(Result configuration)
	{
		m_configuration = configuration;
		
		if(m_task != null)
			m_task.configuration(configuration);
	}
	
	public Result configuration()
	{
		if(m_task != null)
			return m_task.configuration();
		return m_configuration;
	}
}
