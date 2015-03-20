package backend.system.execution;

import backend.model.descriptor.Describable;
import backend.model.result.Result;

public abstract class Task <T extends Result> implements Runnable, Describable{
	public abstract T result();
	public abstract void execute();
	
	public void run()
	{
		execute();
	}
}
