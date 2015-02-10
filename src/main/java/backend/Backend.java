package backend;

import java.util.HashMap;
import java.util.List;

import backend.model.GenericService;
import backend.model.SPHPC.FinanceServiceProvider;

public class Backend {
	
	FinanceServiceProvider m_serviceProvider;

	public Backend()
	{
		m_serviceProvider = new FinanceServiceProvider();
	}
	
	public HashMap<String, GenericService.ServiceDescriptor> services()
	{
		return m_serviceProvider.services();
	}
}
