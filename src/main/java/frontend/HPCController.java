package frontend;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backend.Backend;
import backend.model.GenericService;

@Component
@Controller
@RestController
public class HPCController{
	
	private Backend m_backend = new Backend();
	
    @RequestMapping(value = "/service", method = RequestMethod.GET)
    public HashMap<String, GenericService.ServiceDescriptor> addJob() {
    	return m_backend.services();
    }

}
