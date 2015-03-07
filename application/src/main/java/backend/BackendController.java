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
import backend.model.service.ServicePlugin;
import backend.model.service.ServiceRepository;
import backend.model.serviceprovider.GenericServiceProvider;
import backend.system.GlobalPersistenceUnit;
import backend.system.GlobalState;
import backend.system.JoinClassLoader;
import backend.system.PluginEntityManagerFactory;

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
    public void loadPugins()
    {
	    m_backend.updatePlugins();
    }

}
