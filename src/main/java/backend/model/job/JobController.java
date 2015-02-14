package backend.model.job;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Iterable<GenericJob> serviceProviders() {
    	Iterable<GenericJob> result =  m_jobRepository.findAll();
    	
    	return result;
    }
	
}
