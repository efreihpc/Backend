package backend.model.SPHPC;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Transient;

import ro.fortsoft.pf4j.Extension;
import backend.model.job.JobPlugin;
import backend.model.result.JsonResult;
import backend.system.MongoPersistenceUnit;

@Extension
@Entity
@Inheritance
public class MonteCarloJob extends JobPlugin<JsonResult> {

	@Transient
	MongoPersistenceUnit m_mongoPersistence;
	
	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	public void setMongoPersistence(MongoPersistenceUnit mongoPersistence) {
		m_mongoPersistence = mongoPersistence;
		result(new JsonResult(m_mongoPersistence));	
	}

}
