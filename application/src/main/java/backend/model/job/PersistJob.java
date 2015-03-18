package backend.model.job;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import backend.model.result.DictionaryResult;


@Entity
@Inheritance 

public class PersistJob extends JobEntity<DictionaryResult> {

	@Transient
	JobPersistenceUnit m_jobPersistence;
	
	@OneToOne(fetch = FetchType.EAGER)
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
		m_jobPersistence.save(this.m_jobToPersist);	
	}
	
	public void jobToPersist(JobEntity job)
	{
		m_jobToPersist = job;
	}
}
