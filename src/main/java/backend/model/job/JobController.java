package backend.model.job;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backend.model.service.GenericService;
import backend.system.GlobalState;

@Component
@Controller
@RestController
@RequestMapping("/job")
public class JobController {

	JobRepository m_jobRepository;
	
	public JobController()
	{
		JobPersistenceUnit persistence = GlobalState.get("GlobalPersistenceUnit");
		m_jobRepository = persistence.jobRepository();
	}
	
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<GenericJob> serviceProviders() {
    	Iterable<GenericJob> result =  m_jobRepository.findAll();
    	
    	return result;
    }
    
    @RequestMapping(value = "/id/{identifier}", method = RequestMethod.GET)
    public Iterable<GenericJob> byId(@PathVariable String identifier) {
    	Iterable<GenericJob> result =  m_jobRepository.findById(Long.parseLong(identifier));
    	return result;
    }
    
    @RequestMapping(value = "/delete/id/{identifier}", method = RequestMethod.GET)
    public void deleteById(@PathVariable String identifier) {
    	Iterable<GenericJob> jobs = m_jobRepository.findById(Long.parseLong(identifier));
    	
    	for(GenericJob job : jobs)
    	{
    		m_jobRepository.delete(job);
    	}
    }
	
}
