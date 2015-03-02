package backend.model.job;


public interface Job extends Runnable {
	public void executor(JobExecutor executor);
	public JobExecutor executor();

}
