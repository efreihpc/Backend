package backend;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backend.model.service.GenericService;

@Component
@Controller
@RestController
public class BackendController{
	
	private Backend m_backend = new Backend();
	
    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public HashMap<String, GenericService.ServiceDescriptor> services() {
    	return m_backend.services();
    }
    
    @RequestMapping(value = "/service/{identifier}", method = RequestMethod.GET)
    public GenericService.ServiceDescriptor service(@PathVariable String identifier) {
    	return m_backend.serviceDescriptor(identifier);
    }
    
    @RequestMapping(value = "/schedule/{identifier}", method = RequestMethod.GET)
    public void schedule(@PathVariable String identifier) {
    	m_backend.schedule(identifier);
    }

}
