package backend.model.job;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import ro.fortsoft.pf4j.ExtensionPoint;
import backend.model.result.Result;

@Entity
@Inheritance
public abstract class JobPlugin <T extends Result> extends JobEntity<T> implements ExtensionPoint{
	
	public JobPlugin()
	{
		super();
    	m_classLoader = "Plugin";
    	descriptor().pluginIdentifier(true);
	}
}
