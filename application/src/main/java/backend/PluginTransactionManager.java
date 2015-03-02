package backend;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.orm.jpa.JpaTransactionManager;

public class PluginTransactionManager extends JpaTransactionManager {

	public PluginTransactionManager(EntityManagerFactory emf)
	{
		super(emf);
	}
	
	public EntityManager transactionEntityManager()
	{
		return createEntityManagerForTransaction();
	}
}
