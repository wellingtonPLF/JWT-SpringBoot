package project.br.useAuthentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import project.br.useAuthentication.filter.JwtAuthenticationFilter;
import project.br.useAuthentication.handler.JwtEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;
	@Autowired
    private JwtEntryPoint jwtEntryPoint;
		
	private static final String[] PERMIT_LIST_URLS = {
			"/usuarios/**",
	};
		
	@Bean
	public SecurityFilterChain securityASFilterChain (HttpSecurity http) throws Exception {
		http
        .authorizeHttpRequests()
        .requestMatchers(PERMIT_LIST_URLS).permitAll()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .csrf().disable();
		return http.build();
	}
}
