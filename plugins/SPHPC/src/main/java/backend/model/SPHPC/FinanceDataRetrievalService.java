package backend.model.SPHPC;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import ro.fortsoft.pf4j.Extension;
import backend.model.result.JsonResult;
import backend.model.service.ServicePlugin;

@Extension
@Entity
@Inheritance
public class FinanceDataRetrievalService extends ServicePlugin<JsonResult> {

	public FinanceDataRetrievalService()
	{
		commonName("Finance Data Retrieval Service");
	}
	
	@Override
	public void execute() {
		QuandlDataRetrievalJob job = new QuandlDataRetrievalJob();
		result(job.result());
		
		executeJob(job);
	}

	@Override
	protected void configured() {
		// TODO Auto-generated method stub
		
	}

}
