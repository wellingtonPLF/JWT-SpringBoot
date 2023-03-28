package project.br.useAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.util.CookieUtil;

@Service
public class LogOutService implements LogoutHandler {

	@Autowired
	private TokenService tokenService;
	@Value("${security.jwt.tokenName}")
	private String token;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		final Cookie cookie = WebUtils.getCookie(request, this.token);
		final String jwt = (cookie != null) ? cookie.getValue() : null;
		TokenJPA jwtDB = tokenService.findByToken(jwt);
	    CookieUtil.clear(response, this.token);	    
	    tokenService.remove(jwtDB.getId());;
	    SecurityContextHolder.clearContext();
	}
}
