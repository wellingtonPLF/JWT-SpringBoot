package project.br.useAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.br.useAuthentication.repository.TokenRepository;
import project.br.useAuthentication.util.CookieUtil;

@Service
public class LogOutService implements LogoutHandler {

	@Autowired
	private TokenRepository tokenRepository;
	@Value("${security.jwt.tokenName}")
	private String token;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		final String authHeader = request.getHeader("Authorization");
	    final String jwt;
	    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
	      return;
	    }
	    jwt = authHeader.substring(7);
	    var storedToken = tokenRepository.findByToken(jwt).orElse(null);
	    CookieUtil.clear(response, this.token);	    
	    if (storedToken != null) {
	      storedToken.setExpired(true);
	      storedToken.setRevoked(true);
	      tokenRepository.save(storedToken);
	      SecurityContextHolder.clearContext();
	    }
	}
}
