package backend.model.job;

import backend.model.Task;
import backend.model.result.Result;


public interface Job<T extends Result> extends Runnable, Task<T> {
	public void executor(JobExecutor executor);
	public JobExecutor executor();
}
