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
	JobRepository m_jobRepository;
	
	@OneToOne(fetch = FetchType.EAGER)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	JobEntity m_jobToPersist;
	
	public PersistJob()
	{
		commonName("PersistJob");
	}
	
	public void jobRepository(JobRepository repository)
	{
		m_jobRepository = repository;
	}
	
	@Override
	protected void execute() {
		m_jobRepository.save(this.m_jobToPersist);	
	}
	
	public void jobToPersist(JobEntity job)
	{
		m_jobToPersist = job;
	}
}
