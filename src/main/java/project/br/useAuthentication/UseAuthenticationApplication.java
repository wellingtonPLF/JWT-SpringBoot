package project.br.useAuthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
public class UseAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UseAuthenticationApplication.class, args);
	}
}
