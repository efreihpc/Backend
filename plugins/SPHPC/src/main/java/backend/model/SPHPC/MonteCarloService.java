package backend.model.SPHPC;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import ro.fortsoft.pf4j.Extension;
import backend.model.result.DictionaryResult;
import backend.model.service.ServiceEntity;

@Extension
@Entity
@Inheritance
public class MonteCarloService extends ServiceEntity<DictionaryResult> {
	
	public MonteCarloService()
	{
		commonName("Monto Carlo Service");
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

}
