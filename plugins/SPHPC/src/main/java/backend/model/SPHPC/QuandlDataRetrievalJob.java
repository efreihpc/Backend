package backend.model.SPHPC;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import ro.fortsoft.pf4j.Extension;
import backend.model.job.JobEntity;
import backend.model.result.JsonResult;

@Extension
@Entity
@Inheritance
public class QuandlDataRetrievalJob extends JobEntity<JsonResult> {

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		
	}

}
