package backend.model.job;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import backend.model.GenericJob;
import backend.model.GenericService;
import backend.model.Result;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("ChainJob")
public class ChainJob<T extends Result<U>, U> extends GenericJob<T> {
	
    @OneToOne(fetch = FetchType.EAGER, targetEntity = GenericService.class)
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

