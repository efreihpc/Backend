package backend.model.service;

import backend.model.result.Result;
import backend.model.task.Task;
import backend.system.GlobalPersistenceUnit;
import backend.system.execution.ThreadPoolExecutor;

// T defines the type of the returned result

public abstract class Service<T extends Result> extends Task<T> {
	
	public abstract GlobalPersistenceUnit persistenceUnit();
	
	public abstract void jobExecutor(ThreadPoolExecutor jobExecutor);
	public abstract ThreadPoolExecutor jobExecutor();

	public abstract void persistenceUnit(GlobalPersistenceUnit persistenceUnit);
	
}
