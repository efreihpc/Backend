package backend.model.job;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;

import backend.model.result.Result;
import backend.model.service.ServiceEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@Inheritance 

public class ChainJob<T extends Result> extends JobEntity<T> {
	
	@JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, targetEntity = ServiceEntity.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	JobEntity<T> lastJob;

	public ChainJob()
	{
		commonName("ChainJob");
	}
	
	protected void execute() {

	}
	
	public void add(JobEntity<T> nextJob)
	{		
		if(lastJob == null)
		{
			lastJob = nextJob;
			addSecondaryJob(nextJob);
		}
		else
		{
			lastJob.addSecondaryJob(nextJob);
			lastJob = nextJob;
		}
	}

}

