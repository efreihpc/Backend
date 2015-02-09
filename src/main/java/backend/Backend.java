package backend;

import java.util.List;

import backend.model.SPHPC.FinanceServiceProvider;

public class Backend {
	
	FinanceServiceProvider m_serviceProvider;

	public Backend()
	{
		m_serviceProvider = new FinanceServiceProvider();
	}
	
	public List<String> services()
	{
		return m_serviceProvider.services();
	}
}
