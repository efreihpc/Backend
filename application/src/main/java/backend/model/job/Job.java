package backend.model.job;

import backend.model.result.Result;
import backend.model.task.Task;
import backend.system.execution.ThreadPoolExecutor;


public abstract class Job<T extends Result> extends Task<T> {
	public abstract void executor(ThreadPoolExecutor executor);
	public abstract ThreadPoolExecutor executor();
}
