package backend.model.job;

import backend.model.result.Result;
import backend.system.execution.ThreadPoolExecutor;
import backend.system.execution.Task;


public abstract class Job<T extends Result> extends Task<T> {
	public abstract void executor(ThreadPoolExecutor executor);
	public abstract ThreadPoolExecutor executor();
}
