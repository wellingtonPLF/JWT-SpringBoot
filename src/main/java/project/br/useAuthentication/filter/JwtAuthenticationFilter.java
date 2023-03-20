package project.br.useAuthentication.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.exception.ExpiredJwtExceptionResult;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.TokenJPA;
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
	@Value("${security.jwt.tokenName}")
	private String token;
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) 
					throws ServletException, IOException {
		
		final Cookie cookie = WebUtils.getCookie(request, this.token);
		final String jwt = (cookie != null) ? cookie.getValue() : null;
		final String userID;
		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			TokenJPA tokenDB = tokenRepository.findByToken(jwt).orElseThrow(
				() -> new ExpiredJwtExceptionResult("Token is not valid not found on database")
			);
			var isTokenValid = !tokenDB.isRevoked();
			if (isTokenValid) {
				// (Expired == true) ? null : "userID"
				userID = jwtService.extractSubject(jwt); 
				if (userID == null) {
					throw new ExpiredJwtExceptionResult("Expired Token");
				}
				// NotFoundExceptionResult
				StatusResult<?> result = this.userDetailsService.findById(Long.parseLong(userID)); 
				UserDTO userDetails = (UserDTO) result.getData();
				//BasicAuth
				var authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
						);
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			else {
				new ExpiredJwtExceptionResult("Token is not valid: Something Went wrong");
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