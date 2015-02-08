package backend.model.SPHPC;

import backend.model.Service;
import backend.system.GlobalPersistenceUnit;
import backend.system.JobExecutor;
import backend.system.JobPersistenceUnit;

public class OptimalDistributionService implements Service<SimpleResult> {

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SimpleResult result() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void jobExecutor(JobExecutor exec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dataSource(Service<SimpleResult> dataService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Service<SimpleResult> dataSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persistenceUnit(GlobalPersistenceUnit persistenceUnit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GlobalPersistenceUnit persistenceUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobExecutor jobExecutor() {
		// TODO Auto-generated method stub
		return null;
	}

}
