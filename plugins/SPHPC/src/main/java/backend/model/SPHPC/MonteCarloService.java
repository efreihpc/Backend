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
		addDependency("ad42c7ece9fd7f97a3b7daf3b55460a72c25f98a", "b80b17302e4da7adc8599b9fb302a3b48cbee13e");
	}

	@Override
	public void execute() {
		
		JsonResult deprep = (JsonResult) dependencies().get(0).result();
		System.out.println(deprep.find(""));
				
		MonteCarloJob job = new MonteCarloJob();
		job.setMongoPersistence(persistenceUnit());
		result(job.result());
		
		executeJob(job);
	}

}
