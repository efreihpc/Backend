package model;

import java.util.HashMap;

public interface ServiceProvider {
	
	// returns a hasmap cansisting of a preconfigured Service and the services description
	HashMap<Service, String> services();
	void executeService(Service serviceToExecute);

}
