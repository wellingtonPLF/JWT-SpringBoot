package project.br.useAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import project.br.useAuthentication.service.UserService;

@Configuration 
public class SecurityConfig {
	
	@Autowired
	private UserService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${security.auth.username}")
	private String username;
	
	@Value("${security.auth.password}")
	private String password;
	
	private static final String[] WHITE_LIST_URLS = {
	            "/usuarios/**",
	};
		
	@Bean
	public SecurityFilterChain securityASFilterChain (HttpSecurity http) throws Exception {
		http
        .csrf().disable()
        .authorizeHttpRequests().requestMatchers(WHITE_LIST_URLS).authenticated() 
            .and()
        .httpBasic();
		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder);
	    return authProvider;
	}

	
	/*@Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
            .username(this.username)
            .password(passwordEncoder.encode(this.password))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user);
    }*/
}
