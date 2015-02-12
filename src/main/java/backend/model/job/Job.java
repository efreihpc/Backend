package backend.model.job;

import backend.system.JobExecutor;
import backend.system.JobPersistenceUnit;

public interface Job extends Runnable {
	public void executor(JobExecutor executor);
	public JobExecutor executor();

}
