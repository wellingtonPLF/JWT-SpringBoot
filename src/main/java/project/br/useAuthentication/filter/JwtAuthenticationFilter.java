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
import project.br.useAuthentication.enumState.JwtType;
import project.br.useAuthentication.exception.FilterExceptionResult;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.repository.TokenRepository;
import project.br.useAuthentication.repository.UserRepository;
import project.br.useAuthentication.util.JwtUtil;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
	@Autowired
	private JwtUtil jwtService;
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
				() -> new FilterExceptionResult(JwtType.INVALID_AT)
			);
			if (tokenDB != null) {
				// (Expired == true) ? Exception : "userID"
				userID = jwtService.extractSubject(jwt).orElseThrow(
					() -> new FilterExceptionResult(JwtType.EXPIRED_AT)
				);
				// NotFoundExceptionResult
				UserJPA userDetails = this.userRepository.findById(Long.parseLong(userID)).orElseThrow(
					() -> new FilterExceptionResult(JwtType.INVALID_USER)
				);
				//BasicAuth
				var authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			else {
				throw new FilterExceptionResult(JwtType.INVALID_AT);
			}
			filterChain.doFilter(request, response);
		}
		catch(FilterExceptionResult e) {
			response.setContentType(e.getErrorCode().toString());
			resolver.resolveException(request, response, null, e);
			filterChain.doFilter(request, response);
			return;
		}
	}
}