package backend.model.SPHPC;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import ro.fortsoft.pf4j.Extension;
import backend.model.result.JsonResult;
import backend.model.service.ServicePlugin;

@Extension
@Entity
@Inheritance
public class MonteCarloService extends ServicePlugin<JsonResult> {
	
	public MonteCarloService()
	{
		commonName("Monto Carlo Service");
	}

	@Override
	public void execute() {
		MonteCarloJob job = new MonteCarloJob();
		job.setMongoPersistence(persistenceUnit());
		result(job.result());
		
		executeJob(job);
	}

}
