package backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;
import backend.model.service.ServiceEntity;
import backend.model.serviceprovider.GenericServiceProvider;
import backend.system.GlobalState;

@Component
@Controller
@RestController
@RequestMapping("/backend")
public class BackendController{
	
	private Backend m_backend =  GlobalState.get("Backend");
	
    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    public boolean schedule(@RequestBody ServiceEntity.ServiceDescriptor descriptor) {
    	m_backend.schedule(descriptor);
    	
    	return true;
    }
    
    @RequestMapping(value = "/plugins", method = RequestMethod.GET)
    public List<GenericServiceProvider> loadPugins()
    {
	    PluginManager pluginManager = GlobalState.get("PluginManager");
	    
	    List<GenericServiceProvider> serviceproviders = pluginManager.getExtensions(GenericServiceProvider.class);
	    
	    return serviceproviders;
    }

}
