package backend.model.job;

import backend.model.result.Result;
import backend.system.Task;


public interface Job<T extends Result> extends Runnable, Task<T> {
	public void executor(JobExecutor executor);
	public JobExecutor executor();
}
