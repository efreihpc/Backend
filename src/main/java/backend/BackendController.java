package backend;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.PersistenceProvider;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;
import backend.model.service.ServiceEntity;
import backend.model.service.ServiceRepository;
import backend.model.serviceprovider.GenericServiceProvider;
import backend.system.GlobalState;
import backend.system.PluginEntityManagerFactory;
import backend.system.PluginRepositoryProxyPostProcessor;
import backend.system.PluginRepositorySupport;
import backend.system.PluginTransactionManager;

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
    @Transactional
    public List<GenericServiceProvider> loadPugins()
    {
	    PluginManager pluginManager = GlobalState.get("PluginManager");
	    
	    List<GenericServiceProvider> serviceproviders = pluginManager.getExtensions(GenericServiceProvider.class);
	    
	    List<ServiceEntity> services = pluginManager.getExtensions(ServiceEntity.class);
	    
	    PluginEntityManagerFactory factory = new PluginEntityManagerFactory(services.get(0).getClass().getClassLoader());
	    EntityManager em = factory.createEntityManager();
	    PluginRepositorySupport prs = new PluginRepositorySupport(em);
	    
	    JpaRepositoryFactory repositoryFactory = (JpaRepositoryFactory) prs.repositoryFactorySupport();

	    ServiceRepository repo = repositoryFactory.getRepository(ServiceRepository.class);
	    
    	for(ServiceEntity service: services)
	    {
//		    	em.getTransaction().begin();
	    	repo.save(service);
//		    	em.getTransaction().commit();
	    }
	    	    
	    return serviceproviders;
    }

}
