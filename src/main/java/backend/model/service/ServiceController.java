package backend.model.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backend.system.GlobalState;
import backend.system.ServicePersistenceUnit;

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
	
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Iterable<GenericService> services() {
    	Iterable<GenericService> result =  m_serviceRepository.findAll();
    	System.out.println(result);
    	return result;
    }
	
}
