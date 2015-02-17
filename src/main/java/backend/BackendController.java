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
	
    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public HashMap<String, ServiceEntity.ServiceDescriptor> services() {
    	return m_backend.services();
    }
    
    @RequestMapping(value = "/service/{identifier}", method = RequestMethod.GET)
    public ServiceEntity.ServiceDescriptor service(@PathVariable String identifier) {
    	return m_backend.serviceDescriptor(identifier);
    }
    
    @RequestMapping(value = "/schedule/{identifier}", method = RequestMethod.POST)
    public boolean schedule(@PathVariable String identifier) {
    	m_backend.schedule(identifier);
    	
    	return true;
    }
    
    @RequestMapping(value = "/plugins", method = RequestMethod.GET)
    public void loadPugins()
    {
	    PluginManager pluginManager = new DefaultPluginManager(new File("3rd_party"));
	    pluginManager.loadPlugins();
	    pluginManager.startPlugins();
	    
	    List<ServiceEntity> services = pluginManager.getExtensions(ServiceEntity.class);
	    for (ServiceEntity service : services) {
	        System.out.println(">>> " + service.commonName());
	    }
	    
	    List<GenericServiceProvider> serviceproviders = pluginManager.getExtensions(GenericServiceProvider.class);
	    for (GenericServiceProvider serviceProvider : serviceproviders) {
	        System.out.println(">>> " + serviceProvider.services());
	    }
    }

}
