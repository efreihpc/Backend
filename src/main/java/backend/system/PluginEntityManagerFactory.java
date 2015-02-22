package backend.system;

import java.util.Properties;

import javax.activation.DataSource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

public class PluginEntityManagerFactory extends
		LocalContainerEntityManagerFactoryBean {
	
	ApplicationContext m_context = new ClassPathXmlApplicationContext("Spring-Config.xml");
	EntityManagerFactory m_entityManagerfactory;
	
	public PluginEntityManagerFactory(ClassLoader classLoader)
	{
		setBeanClassLoader(classLoader);
	    setResourceLoader(new PathMatchingResourcePatternResolver(classLoader));
	    setDataSource(m_context.getBean(DriverManagerDataSource.class));
	    setPackagesToScan("backend.model.*");
	    
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(false);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.H2);
	    setJpaVendorAdapter(hibernateJpaVendorAdapter);
	    
	    setPersistenceProvider(new HibernatePersistence());
	    
	    Properties props = new Properties();
	    props.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
	    props.put("hibernate.hbm2ddl.auto", "update");
	    setJpaProperties(props);
	    
		m_entityManagerfactory = createNativeEntityManagerFactory();
	}
	
	public EntityManager createEntityManager()
	{		
		return m_entityManagerfactory.createEntityManager();
	}
	
	public JpaTransactionManager createTransactionManager()
	{
		return new JpaTransactionManager(m_entityManagerfactory);
	}
	
	public RepositoryProxyPostProcessor createProxyPostProcessor()
	{
		return new PluginRepositoryProxyPostProcessor(createTransactionManager());
	}

}
