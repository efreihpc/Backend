package backend.model.SPHPC;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonTypeName;

import backend.model.result.SimpleResult;
import backend.model.service.GenericService;

@Entity
@JsonTypeName("SPHPCOptimalDistributionService")
public class OptimalDistributionService extends GenericService<SimpleResult> {

    @OneToOne
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	private PrototypeJob m_job;
	
	public OptimalDistributionService()
	{
		name("Optimal Distribution Service");
		result(new SimpleResult());
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
