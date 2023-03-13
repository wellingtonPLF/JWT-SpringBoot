package project.br.useAuthentication.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.exception.ExpiredJwtExceptionResult;
import project.br.useAuthentication.repository.TokenRepository;
import project.br.useAuthentication.service.JwtService;
import project.br.useAuthentication.service.UserService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserService userDetailsService;
	@Autowired
	private TokenRepository tokenRepository;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) 
					throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final Object userName;
		if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			jwt = authHeader.substring(7);
			userName = jwtService.extractUsername(jwt); //(Expired == true) ? null : "userName"
			var isTokenValid = tokenRepository.findByToken(jwt)
					.map(t -> !t.isExpired() && !t.isRevoked())
					.orElse(false);
			if (userName != null && isTokenValid) {
				UserDTO userDetails = this.userDetailsService.loadUserByUsername((String) userName); //Exception UserNotFound
				//BasicAuth
				var authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
						);
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			else {
				throw new ExpiredJwtExceptionResult("Token is not valid");
			}
			filterChain.doFilter(request, response);
		}
		catch(Exception e) {
			resolver.resolveException(
					request, 
					response, 
					null, 
					e);
		}
	}	
}