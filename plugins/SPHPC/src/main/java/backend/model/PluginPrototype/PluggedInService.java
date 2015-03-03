package backend.model.PluginPrototype;

import javax.persistence.Entity;

import org.springframework.transaction.annotation.Transactional;

import ro.fortsoft.pf4j.Extension;
import backend.model.result.SimpleResult;
import backend.model.service.ServicePlugin;
import backend.model.serviceprovider.GenericServiceProvider;

@Extension
@Entity
@Transactional
public class PluggedInService extends ServicePlugin<SimpleResult>{
	
	public PluggedInService()
	{
		commonName("Plugged In Service");
	}
	@Override
	public void execute() {
		try 
		{
			GenericServiceProvider provider = serviceProviderRepository().serviceProvider("b80b17302e4da7adc8599b9fb302a3b48cbee13e");
			
			if(provider != null)
				System.out.println(provider.commonName());
			else
				System.out.println("ServiceProvider not found");
			
		}
		catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
