package backend.model.SPHPC;

import com.fasterxml.jackson.annotation.JsonTypeName;

import backend.model.serviceprovider.GenericServiceProvider;

@JsonTypeName("SPHPCFiancialServiceProvider")
public class FinanceServiceProvider extends GenericServiceProvider {
	
	public FinanceServiceProvider()
	{
		super();
		commonName("Financial ServiceProvider");
	}
}
