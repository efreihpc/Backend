package backend.model;

import backend.model.service.ServiceRepository;
import backend.system.GlobalState;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

public class RegisteringPlugin extends Plugin {
	
	
	
	 public RegisteringPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	 public void start() {
		System.out.println("Registering Plugin");
		GlobalPersistenceUnit globalPersistence = 	GlobalState.get("GlobalPersistenceUnit");
		globalPersistence.servicePersistence().registerPluginClassLoader(this.getClass().getClassLoader(), ServiceRepository.class);
		globalPersistence.serviceProviderRepository().scan();
	 }

}
