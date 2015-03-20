package backend.system.execution;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import backend.model.descriptor.Describable;
import backend.model.result.Result;

@Entity
@Inheritance
public abstract class Task <T extends Result> implements Runnable, Describable{
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long m_id;
	
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
}
