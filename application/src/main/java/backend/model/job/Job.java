package backend.model.job;


public interface Job<T> extends Runnable {
	public void executor(JobExecutor executor);
	public JobExecutor executor();

	public T result();
}
