package frontend;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class HPCApplication {

	public static void main(String[] args) {
		SpringApplication.run(HPCApplication.class, args);

	}

}
