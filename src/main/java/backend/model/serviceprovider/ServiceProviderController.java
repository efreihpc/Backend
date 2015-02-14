package backend.model.serviceprovider;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backend.system.GlobalState;

@Component
@Controller
@RestController
@RequestMapping("/serviceprovider")
public class ServiceProviderController {

	ServiceProviderRepository m_serviceProviderRepository;
	
	public ServiceProviderController()
	{
		ServiceProviderPersistenceUnit persistence = GlobalState.get("GlobalPersistenceUnit");
		m_serviceProviderRepository = persistence.serviceProviderRepository();
	}
	
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Iterable<GenericServiceProvider> serviceProviders() {
    	Iterable<GenericServiceProvider> result =  m_serviceProviderRepository.findAll();
    	
    	System.out.println(result);
    	return result;
    }
	
}
