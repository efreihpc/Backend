package backend.model.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backend.system.GlobalState;

@Component
@Controller
@RestController
@RequestMapping("/service")
public class ServiceController {

	ServiceRepository m_serviceRepository;
	
	public ServiceController()
	{
		ServicePersistenceUnit persistence = GlobalState.get("GlobalPersistenceUnit");
		m_serviceRepository = persistence.serviceRepository();
	}
	
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<GenericService> services() {
    	Iterable<GenericService> result =  m_serviceRepository.findAll();
    	return result;
    }
    
    @RequestMapping(value = "/id/{identifier}", method = RequestMethod.GET)
    public Iterable<GenericService> getById(@PathVariable String identifier) {
    	Iterable<GenericService> result =  m_serviceRepository.findById(Long.parseLong(identifier));
    	return result;
    }
    
    @RequestMapping(value = "/delete/id/{identifier}", method = RequestMethod.GET)
    public void deleteById(@PathVariable String identifier) {
    	Iterable<GenericService> services = m_serviceRepository.findById(Long.parseLong(identifier));
    	
    	for(GenericService service : services)
    	{
    		m_serviceRepository.delete(service);
    	}
    }
	
}
