package backend.system;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import backend.model.service.ServiceRepository;
import backend.model.serviceprovider.GenericServiceProvider;
import ro.fortsoft.pf4j.Plugin;
import ro.fortsoft.pf4j.PluginWrapper;

public class RegisteringPlugin extends Plugin {
	
	
	
	 public RegisteringPlugin(PluginWrapper wrapper) {
		super(wrapper);
	}

	@Override
	 public void start() {
		GlobalPersistenceUnit globalPersistence = 	GlobalState.get("GlobalPersistenceUnit");
		ClassLoader localPluginLoader = this.getClass().getClassLoader();
				
		globalPersistence.servicePersistence().registerPluginClassLoader(localPluginLoader, ServiceRepository.class);

    	// create scanner and disable default filters (that is the 'false' argument)
    	final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
    	provider.setResourceLoader(new PathMatchingResourcePatternResolver(localPluginLoader));
    	
    	// add include filters which matches all the classes (or use your own)
    	provider.addIncludeFilter((TypeFilter) new AssignableTypeFilter(GenericServiceProvider.class));
    	// get matching classes defined in the package
    	final Set<BeanDefinition> classes = provider.findCandidateComponents(this.getClass().getPackage().getName());
    	
    	for (BeanDefinition definition : classes) {
			try 
			{
				Class<GenericServiceProvider> registeredClass;
				registeredClass = (Class<GenericServiceProvider>) localPluginLoader.loadClass(definition.getBeanClassName());

				GenericServiceProvider instance = registeredClass.newInstance();		
				instance.persistenceUnit(globalPersistence);
				globalPersistence.serviceProviderRepository().registerServiceProvider(instance);
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			} 
			catch (IllegalArgumentException e) 
			{
				e.printStackTrace();
			} 
			catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	 }
	}

}
