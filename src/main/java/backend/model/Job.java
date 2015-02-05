package backend.model;

import backend.system.JobPersistenceUnit;

public interface Job extends Runnable {
	
	public void persistenceUnit(JobPersistenceUnit persistenceUnit);

}
