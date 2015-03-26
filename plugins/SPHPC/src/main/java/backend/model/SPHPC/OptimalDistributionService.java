package backend.model.SPHPC;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import ro.fortsoft.pf4j.Extension;
import backend.model.result.DictionaryResult;
import backend.model.result.Result;
import backend.model.service.ServicePlugin;

import com.fasterxml.jackson.annotation.JsonProperty;

@Extension
@Entity
@Inheritance
public class OptimalDistributionService extends ServicePlugin<DictionaryResult> {

	@Transient
	public PrototypeJob m_job;
	
	public OptimalDistributionService()
	{
		commonName("Optimal Distribution Service");
		m_job = new PrototypeJob();
		result(new DictionaryResult());
	}
		
	@Override
	public void execute() {
		executeJob(m_job);
	}

	@Override
	@JsonProperty("result")
	public DictionaryResult result() {
		return m_job.result();
	}

	@Override
	protected void configured() {
		// TODO Auto-generated method stub
		
	}
}
