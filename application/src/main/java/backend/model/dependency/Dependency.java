package backend.model.dependency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import backend.model.result.Result;
import backend.system.execution.Task;

@Entity
@Inheritance
public abstract class Dependency<T extends Task<U>, U extends Result>  implements DependencyInterface<T, U> {
		
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long m_id;
	
	@Transient
	protected T m_task;
	
	@Override
	public void task(T task)
	{
		m_task = task;
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
}
