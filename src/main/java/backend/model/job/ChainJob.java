package backend.model.job;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;

import backend.model.result.Result;
import backend.model.service.GenericService;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@Inheritance 

public class ChainJob<T extends Result> extends GenericJob<T> {
	
	@JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, targetEntity = GenericService.class)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.ALL)
	GenericJob<T> lastJob;

	public ChainJob()
	{
		commonName("ChainJob");
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

