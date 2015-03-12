package backend.model.job;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backend.model.service.ServiceEntity;
import backend.system.GlobalPersistenceUnit;
import backend.system.GlobalState;

//TODO: generalize Controller
@Component
@Controller
@RestController
@RequestMapping("/job")
public class JobController {

	JobPersistenceUnit m_jobPersistence;
	
	public JobController()
	{
		GlobalPersistenceUnit persistence = GlobalState.get("GlobalPersistenceUnit");
		m_jobPersistence = persistence.jobPersistence();
	}
	
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<JobEntity> jobs() {
    	List<JobEntity> result =  m_jobPersistence.localServiceRepository().findByClassLoader("Default");
    	result.addAll(m_jobPersistence.pluginServiceRepository().findByClassLoader("Plugin"));
    	return result;
    }
    
    @RequestMapping(value = "/id/{identifier}", method = RequestMethod.GET)
    public List<JobEntity> getById(@PathVariable String identifier) {
    	List<JobEntity> result =  m_jobPersistence.localServiceRepository().findById(Long.parseLong(identifier));
    	result.addAll(m_jobPersistence.pluginServiceRepository().findById(Long.parseLong(identifier)));
    	return result;
    }
    
    @RequestMapping(value = "/delete/id/{identifier}", method = RequestMethod.GET)
    public void deleteById(@PathVariable String identifier) {
    	List<JobEntity> jobs = m_jobPersistence.localServiceRepository().findById(Long.parseLong(identifier));
    	jobs.addAll(m_jobPersistence.pluginServiceRepository().findById(Long.parseLong(identifier)));
    	
    	for(JobEntity job : jobs)
    	{
    		m_jobPersistence.delete(job);
    	}
    }
	
}
