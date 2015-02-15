package backend.model.SPHPC;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import backend.model.job.ChainJob;
import backend.model.result.SimpleResult;
import backend.model.service.GenericService;

@Entity
@Inheritance   

public class MonteCarloService<T> extends GenericService<SimpleResult> {

	public MonteCarloService()
	{
		commonName("Monte Carlo Service");
	}
	
	@Override
	public void execute() {
		ChainJob<SimpleResult> chain = new ChainJob<SimpleResult>();
		executeJob(chain);		
	}

}
