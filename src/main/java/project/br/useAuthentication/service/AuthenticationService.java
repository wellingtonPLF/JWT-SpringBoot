package project.br.useAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.br.useAuthentication.dtoModel.AuthDTO;
import project.br.useAuthentication.dtoModel.TokenDTO;
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.enumState.JwtType;
import project.br.useAuthentication.enumState.TokenEnum;
import project.br.useAuthentication.enumState.TokenType;
import project.br.useAuthentication.exception.AuthenticationExceptionResponse;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.repository.TokenRepository;
import project.br.useAuthentication.repository.UserRepository;
import project.br.useAuthentication.util.CookieUtil;
import project.br.useAuthentication.util.JwtUtil;

@Service
public class AuthenticationService {
	
	@Value("${security.jwt.tokenName}")
	private String token;
	@Autowired
	private JwtUtil jwtService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private TokenService tokenService;

	public StatusResult<?> register(UserDTO user) {
		this.userService.insertUpdate(user);
	    return new StatusResult<String>(HttpStatus.OK.value(), "Sing up was successfully made it.");
	}

	public StatusResult<?> authenticate(AuthDTO auth) {
		try {
			UserDTO user = this.userService.loadUserByEmail(auth.getEmail());
			Boolean valid = this.passwordEncoder.matches(auth.getPassword(), user.getPassword());
			if(!valid) {
				throw new UsernameNotFoundException("Incorrect Email or Password , try again.");
			}
			String jwtToken = jwtService.generateToken(user, TokenEnum.ACCESS_TOKEN);
			String refreshToken = jwtService.generateToken(user, TokenEnum.REFRESH_TOKEN);
			response.setContentType(null);
			TokenJPA jwt = new TokenJPA(jwtToken, TokenType.BEARER, false, false, new UserJPA(user));
			
			CookieUtil.create(response, this.token, jwtToken, false, -1, "localhost");
			CookieUtil.create(response, "refreshToken", refreshToken, false, -1, "localhost");
			
			revokeUserToken(user);
			this.tokenService.insertUpdate(jwt);
			
			return new StatusResult<String>(HttpStatus.OK.value(), user.getId().toString());
		}
		catch (Exception e) {
			throw new UsernameNotFoundException("Incorrect Email or Password , try again.");
		}
	}
	
	public StatusResult<?> refresh(){
		final Cookie cookieAccess = WebUtils.getCookie(request, "token");
		final String accessToken = (cookieAccess != null) ? cookieAccess.getValue() : null;
		final TokenJPA jwt = this.tokenRepository.findByToken(accessToken).orElseThrow(
			() -> new AuthenticationExceptionResponse(JwtType.INVALID_AT.toString())
		);
		final String expiredAcessToken = jwtService.extractSubject(jwt.getToken()).orElse(null);
		if (expiredAcessToken == null) {
			final Cookie cookieRefresh = WebUtils.getCookie(request, "refreshToken");
			final String refreshToken = (cookieRefresh != null) ? cookieRefresh.getValue() : null;
			if (refreshToken == null) {
				throw new AuthenticationExceptionResponse(JwtType.INVALID_RT.toString());
			}
			final String userID = jwtService.extractSubject(refreshToken).orElseThrow(
					() -> new AuthenticationExceptionResponse(JwtType.EXPIRED_RT.toString())
			);
			UserJPA u = this.userRepository.findById(Long.parseLong(userID)).orElseThrow(
					() -> new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString())
			);
			UserDTO user = new UserDTO(u);
			String jwtToken = jwtService.generateToken(user, TokenEnum.ACCESS_TOKEN);
			String jwtRefresh = jwtService.generateToken(user, TokenEnum.REFRESH_TOKEN);
			jwt.setToken(jwtToken);
			
			CookieUtil.create(response, "token", jwtToken, false, -1, "localhost");
			CookieUtil.create(response, "refreshToken", jwtRefresh , false, -1, "localhost");
			revokeUserToken(user);
			
			this.tokenService.insertUpdate(jwt);
			return new StatusResult<String>(HttpStatus.OK.value(), "Refresh Succefully Done");
		}
		else {
			throw new AuthenticationExceptionResponse("Access Token not expired, also can't be refreshed");
		}
	}

	private void revokeUserToken(UserDTO user) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty()) {
			return;
		}
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}
}
