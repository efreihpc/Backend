package backend.model.PluginPrototype;

import ro.fortsoft.pf4j.Extension;
import backend.model.serviceprovider.GenericServiceProvider;

@Extension
public class PluggedInServiceProvider extends GenericServiceProvider {
	
	public PluggedInServiceProvider()
	{
		commonName("Plugged In ServiceProvider");
	}
}
