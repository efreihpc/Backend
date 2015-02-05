package backend.model;

import backend.system.JobExecutor;
import backend.system.JobPersistenceUnit;

// T defines the type of the returned result
public interface Service<T> {
	
	public void execute();
	public T result();
	
	public void jobExecutor(JobExecutor exec);
	public void persistenceUnit(JobPersistenceUnit persistenceUnit);
	
	//set the datasource thats used by the service 
	public void dataSource(Service<T> dataService);
	
	public Service<T> dataSource();

}
