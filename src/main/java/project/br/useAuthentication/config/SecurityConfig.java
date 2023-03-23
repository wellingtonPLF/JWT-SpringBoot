package project.br.useAuthentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import project.br.useAuthentication.filter.JwtAuthenticationFilter;
import project.br.useAuthentication.handler.JwtEntryPoint;
import project.br.useAuthentication.service.LogOutService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;
	@Autowired
    private JwtEntryPoint jwtEntryPoint;
	@Autowired
	private LogOutService logoutHandler;
		
	private static final String[] PERMIT_LIST_URLS = {
			"/usuarios/**",
	};
		
	@Bean
	public SecurityFilterChain securityASFilterChain (HttpSecurity http) throws Exception {
		http
        .csrf().disable()
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
        .logout()
        .logoutUrl("/usuarios/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());
		return http.build();
	}
}
