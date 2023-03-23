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
import project.br.useAuthentication.enumState.RoleName;
import project.br.useAuthentication.exception.ExpiredJwtExceptionResult;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.repository.TokenRepository;
import project.br.useAuthentication.repository.UserRepository;
import project.br.useAuthentication.service.UserService;
import project.br.useAuthentication.util.JwtUtil;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
	@Autowired
	private JwtUtil jwtService;
	@Autowired
	private UserService userDetailsService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenRepository tokenRepository;
	@Value("${security.jwt.tokenName}")
	private String token;
	
	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) 
					throws ServletException, IOException {
		final String userID;
		final Cookie cookie = WebUtils.getCookie(request, this.token);
		final String jwt = (cookie != null) ? cookie.getValue() : null;
		try {
			TokenJPA tokenDB = tokenRepository.findByToken(jwt).orElseThrow(
				() -> new Exception("Token not in Database, you must SignIn!")
			);
			Boolean isTokenValid = !tokenDB.isRevoked();
			if (isTokenValid) {
				// (Expired == true) ? Exception : "userID"
				userID = jwtService.extractSubject(jwt).orElseThrow(
					() -> new Exception("Expired Token!")
				);
				// NotFoundExceptionResult
				var result = this.userRepository.findById(Long.parseLong(userID)).orElseThrow(
					() -> new Exception("Sub userID not found!")
				);
				UserDTO userDetails = new UserDTO(result);
				//BasicAuth
				var authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			else {
				new ExpiredJwtExceptionResult("Token is not valid");
			}
			filterChain.doFilter(request, response);
		}
		catch(Exception e) {
			//String x = RoleName.ROLE_ADMIN.toString();
			response.setContentType(e.getLocalizedMessage());
			resolver.resolveException(
				request, 
				response, 
				null, 
			e);
			filterChain.doFilter(request, response);
			return;
		}
	}
}