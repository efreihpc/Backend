package backend.model.service;

import backend.model.job.JobExecutor;
import backend.model.result.Result;
import backend.system.GlobalPersistenceUnit;
import backend.system.Task;

// T defines the type of the returned result

public interface Service<T extends Result> extends Task<T> {
	
	void persistenceUnit(GlobalPersistenceUnit persistenceUnit);
	GlobalPersistenceUnit persistenceUnit();
	
	void jobExecutor(JobExecutor jobExecutor);
	JobExecutor jobExecutor();
}
