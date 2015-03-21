package backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;
import backend.system.GlobalState;

//@Configuration
@EnableAutoConfiguration
@ComponentScan
public class HPCApplication {
//
//    @Bean
//    Environment env() {
//        return new Environment();
//    }
//	
//    @Bean
//    Reactor createReactor(Environment env) {
//        return Reactors.reactor()
//                .env(env)
//                .dispatcher(Environment.THREAD_POOL)
//                .get();
//    }
	
	public static void main(String[] args) 
	{		
		GlobalState.set("Backend", new Backend());
		
		SpringApplication.run(HPCApplication.class, args);

	}

}
