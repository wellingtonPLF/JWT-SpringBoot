package project.br.useAuthentication;

/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
*/

// @Configuration 
public class SecurityConfig {
	
	/*@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain securityASFilterChain (HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		return http.formLogin().and().build();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
		return http.formLogin()
				.and().authorizeHttpRequests()
				.anyRequest().authenticated().and().build();
	}  
	   
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}*/
}
