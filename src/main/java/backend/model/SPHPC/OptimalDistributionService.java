package backend.model.SPHPC;

import backend.model.GenericService;
import backend.model.result.SimpleResult;

public class OptimalDistributionService extends GenericService<SimpleResult> {

	private PrototypeJob m_job;
	
	public OptimalDistributionService()
	{
		name("Optimal Distribution Service");
	}
		
	@Override
	public void execute() {
		m_job = new PrototypeJob();
		executeJob(m_job);
	}

	@Override
	public SimpleResult result() {
		return m_job.result();
	}
}
