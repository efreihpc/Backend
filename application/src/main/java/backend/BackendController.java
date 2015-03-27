package backend;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import frontend.ServiceTransfer;
import backend.model.descriptor.ServiceDescriptor;
import backend.model.result.DictionaryResult;
import backend.model.service.ServiceEntity;
import backend.system.GlobalState;

@Component
@Controller
@RestController
@RequestMapping("/backend")
public class BackendController{

	private Backend m_backend =  GlobalState.get("Backend");

    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public ServiceEntity schedule(@RequestBody ServiceTransfer service) {
    	return m_backend.schedule(service.descriptor(), DictionaryResult.fromStringMap(service.configuration()));
    }

    @RequestMapping(value = "/plugins", method = RequestMethod.GET)
    public void loadPugins()
    {
	    m_backend.updatePlugins();
    }

}
