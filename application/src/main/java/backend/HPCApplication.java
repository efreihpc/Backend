package backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import backend.system.GlobalState;

@ComponentScan
@EnableAutoConfiguration
public class HPCApplication {

	public static void main(String[] args) 
	{		
		GlobalState.set("Backend", new Backend());
		
		SpringApplication.run(HPCApplication.class, args);

	}

}
