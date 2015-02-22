package backend.system;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.transaction.TransactionManager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;

public class PluginRepositorySupport<T extends Repository<S,ID>,S,ID extends Serializable> extends
		TransactionalRepositoryFactoryBeanSupport<T, S, ID> {
	
	private EntityManager m_entityManager;
	
	public PluginRepositorySupport(EntityManager em)
	{
		m_entityManager = em;
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Config.xml");
		setBeanFactory(context);
		setTransactionManager("transactionManager");
	}

	@Override
	protected RepositoryFactorySupport doCreateRepositoryFactory() {
		return new JpaRepositoryFactory(m_entityManager);
	}
	public RepositoryFactorySupport repositoryFactorySupport()
	{
		return createRepositoryFactory();
	}

}
