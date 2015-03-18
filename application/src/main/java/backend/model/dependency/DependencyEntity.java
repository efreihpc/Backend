package backend.model.dependency;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import backend.model.Descriptor;
import backend.model.Task;
import backend.model.result.Result;

@Entity
@Inheritance

public abstract class DependencyEntity<T extends Task<U>, U extends Result>  implements Dependency<T, U> {
		
	T m_Task;
	
	public DependencyEntity(String taskIdentifier)
	{
		
	}

	@Override
	public U result() {
		return m_Task.result();
	}

	@Override
	public Descriptor descriptor() {
		return m_Task.descriptor();
	}
}
