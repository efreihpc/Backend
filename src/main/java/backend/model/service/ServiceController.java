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

	ServicePersistenceUnit m_persistence;
	
	public ServiceController()
	{
		m_persistence = GlobalState.get("GlobalPersistenceUnit");
	}
	
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<ServiceEntity> services() {
    	Iterable<ServiceEntity> result =  m_serviceRepository.findAll();
    	return result;
    }
    
    @RequestMapping(value = "/id/{identifier}", method = RequestMethod.GET)
    public Iterable<ServiceEntity> getById(@PathVariable String identifier) {
    	Iterable<ServiceEntity> result =  m_serviceRepository.findById(Long.parseLong(identifier));
    	return result;
    }
    
    @RequestMapping(value = "/delete/id/{identifier}", method = RequestMethod.GET)
    public void deleteById(@PathVariable String identifier) {
    	Iterable<ServiceEntity> services = m_serviceRepository.findById(Long.parseLong(identifier));
    	
    	for(ServiceEntity service : services)
    	{
    		m_serviceRepository.delete(service);
    	}
    }
	
}
