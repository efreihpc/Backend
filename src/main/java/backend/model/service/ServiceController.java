package backend.model.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import backend.model.GlobalPersistenceUnit;
import backend.system.GlobalState;

@Component
@Controller
@RestController
@RequestMapping("/service")
public class ServiceController {

	GlobalPersistenceUnit m_persistence;
	ServicePersistenceUnit m_servicePersistence;
	
	public ServiceController()
	{
		m_persistence = GlobalState.get("GlobalPersistenceUnit");
		m_servicePersistence = m_persistence.servicePersistence();
	}
	
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ServiceEntity> services() {
    	List<ServiceEntity> result =  m_servicePersistence.localServiceRepository().findByClassLoader("Default");
    	result.addAll(m_servicePersistence.pluginServiceRepository().findByClassLoader("Plugin"));
    	return result;
    }
    
    @RequestMapping(value = "/id/{identifier}", method = RequestMethod.GET)
    public List<ServiceEntity> getById(@PathVariable String identifier) {
    	List<ServiceEntity> result =  m_servicePersistence.localServiceRepository().findById(Long.parseLong(identifier));
    	result.addAll(m_servicePersistence.pluginServiceRepository().findById(Long.parseLong(identifier)));
    	return result;
    }
    
    @RequestMapping(value = "/delete/id/{identifier}", method = RequestMethod.GET)
    public void deleteById(@PathVariable String identifier) {
    	List<ServiceEntity> services = m_servicePersistence.localServiceRepository().findById(Long.parseLong(identifier));
    	services.addAll(m_servicePersistence.pluginServiceRepository().findById(Long.parseLong(identifier)));
    	
    	for(ServiceEntity service : services)
    	{
    		m_servicePersistence.delete(service);
    	}
    }
	
}
