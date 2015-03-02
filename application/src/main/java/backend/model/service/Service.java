package backend.model.service;

import javax.persistence.Entity;

import backend.model.GlobalPersistenceUnit;
import backend.model.job.JobExecutor;
import backend.model.job.JobPersistenceUnit;

// T defines the type of the returned result

public interface Service<T> {
	
	public void persistenceUnit(GlobalPersistenceUnit persistenceUnit);
	public GlobalPersistenceUnit persistenceUnit();
	
	public void jobExecutor(JobExecutor jobExecutor);
	public JobExecutor jobExecutor();
	
	public void execute();
	public T result();
}
