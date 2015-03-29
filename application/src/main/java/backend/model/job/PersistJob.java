package backend.model.job;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import backend.model.result.DictionaryResult;
import backend.model.result.Result;


@Entity
public class PersistJob extends JobEntity<DictionaryResult> {

	@Transient
	JobPersistenceUnit m_jobPersistence;
	
	@OneToOne(fetch = FetchType.EAGER, targetEntity = JobEntity.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	JobEntity m_jobToPersist;
	
	public PersistJob()
	{
		commonName("PersistJob");
	}
	
	public void jobRepository(JobPersistenceUnit persistence)
	{
		m_jobPersistence = persistence;
	}
	
	@Override
	public void execute() {
		m_jobPersistence.save(m_jobToPersist);	
		System.out.println("PersistJob: Persisted Job: " + m_jobToPersist.descriptor().commonName() + ":" + m_jobToPersist.id());
	}
	
	public void jobToPersist(JobEntity job)
	{
		m_jobToPersist = job;
	}

	@Override
	protected void configured() {
		// TODO Auto-generated method stub
		
	}
}
