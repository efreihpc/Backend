package backend.model.SPHPC;

import backend.model.GenericJob;
import backend.model.JobRepository;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonTypeName;


@Entity
@JsonTypeName("PersistJob")
public class PersistJob extends GenericJob<SimpleResult> {

	@Transient
	JobRepository m_jobRepository;
	
	@OneToOne
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	GenericJob m_jobToPersist;
	
	public PersistJob()
	{
		name("PersistJob");
	}
	
	public void jobRepository(JobRepository repository)
	{
		m_jobRepository = repository;
	}
	
	@Override
	protected void execute() {
		System.out.println("Persisting");
		m_jobRepository.save(this.m_jobToPersist);	
	}
	
	public void jobToPersist(GenericJob job)
	{
		m_jobToPersist = job;
	}
}
