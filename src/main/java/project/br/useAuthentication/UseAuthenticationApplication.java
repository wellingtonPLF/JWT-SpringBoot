package project.br.useAuthentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class UseAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UseAuthenticationApplication.class, args);
	}
}
