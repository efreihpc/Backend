package backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
    	ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Config.xml");
    	SessionFactory sessionFact = (SessionFactory) context.getBean("sessionFactory");    	
    	
	    PluginManager pluginManager = GlobalState.get("PluginManager");
	    
	    List<GenericServiceProvider> serviceproviders = pluginManager.getExtensions(GenericServiceProvider.class);
	    
	    List<ServiceEntity> services = pluginManager.getExtensions(ServiceEntity.class);
	    
	    Configuration configuration = new Configuration();
	    
	    for(ServiceEntity service : services)
	    {
	    	configuration.addAnnotatedClass(service.getClass());
	    }
	    
	    configuration.configure();
	    ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
	    SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	    Session session = sessionFactory.openSession();
	    
	    return serviceproviders;
    }

}
