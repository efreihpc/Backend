package backend.system;

import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

public class PluginRepositoryProxyPostProcessor implements
		RepositoryProxyPostProcessor {
	
	private JpaTransactionManager m_jpaTransactionManager;
	
	public PluginRepositoryProxyPostProcessor(JpaTransactionManager jtm)
	{
		m_jpaTransactionManager = jtm;
	}
	
	@Override
	public void postProcess(ProxyFactory factory,
			RepositoryInformation repositoryInformation) {
		factory.addAdvice(new TransactionInterceptor(m_jpaTransactionManager, new MatchAlwaysTransactionAttributeSource()));
	}

}
