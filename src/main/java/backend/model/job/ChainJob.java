package backend.model.job;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import backend.model.GenericJob;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("ChainJob")
public class ChainJob<T> extends GenericJob<T> {
	
	@OneToOne
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	GenericJob<T> lastJob;

	public ChainJob()
	{
		name("ChainJob");
	}
	
	protected void execute() {

	}
	
	public void add(GenericJob<T> nextJob)
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

