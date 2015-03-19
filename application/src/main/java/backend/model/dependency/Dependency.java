package backend.model.dependency;

import backend.model.Task;
import backend.model.result.Result;

public abstract class Dependency<T extends Task<U>, U extends Result>  implements DependencyInterface<T, U> {
		
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
