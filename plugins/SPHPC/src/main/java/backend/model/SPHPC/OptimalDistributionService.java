package backend.model.SPHPC;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;

import backend.model.job.ChainJob;
import backend.model.result.Result;
import backend.model.result.DictionaryResult;
import backend.model.service.ServiceEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@Inheritance                                                                                                                                                 

public class OptimalDistributionService extends ServiceEntity<DictionaryResult> {

	@OneToOne(fetch = FetchType.EAGER)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	private PrototypeJob m_job;
	
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
}
