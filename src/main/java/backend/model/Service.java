package backend.model;

import backend.system.GlobalPersistenceUnit;
import backend.system.JobExecutor;
import backend.system.JobPersistenceUnit;

// T defines the type of the returned result
public interface Service<T> {
	
	public void persistenceUnit(GlobalPersistenceUnit persistenceUnit);
	public GlobalPersistenceUnit persistenceUnit();
	
	public void jobExecutor(JobExecutor jobExecutor);
	public JobExecutor jobExecutor();
	
	public void execute();
	public T result();
}
