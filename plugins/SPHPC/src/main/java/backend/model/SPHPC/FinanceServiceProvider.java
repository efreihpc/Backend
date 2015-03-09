package backend.model.SPHPC;

import ro.fortsoft.pf4j.Extension;
import backend.model.serviceprovider.GenericServiceProvider;

@Extension
public class FinanceServiceProvider extends GenericServiceProvider {
	
	public FinanceServiceProvider()
	{
		super();
		commonName("Financial ServiceProvider");
	}
}
