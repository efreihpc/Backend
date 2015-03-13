package backend.model.service;

import javax.persistence.Entity;

import backend.model.job.JobExecutor;
import backend.model.job.JobPersistenceUnit;
import backend.system.GlobalPersistenceUnit;

// T defines the type of the returned result

public interface Service<T> {
	
	public void persistenceUnit(GlobalPersistenceUnit persistenceUnit);
	public GlobalPersistenceUnit persistenceUnit();
	
	public void jobExecutor(JobExecutor jobExecutor);
	public JobExecutor jobExecutor();
	
	public void execute();
	public T result();
	
	public void configure(ServiceConfiguration config);
}
