package project.br.useAuthentication.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.br.useAuthentication.enumState.JwtType;
import project.br.useAuthentication.enumState.TokenType;
import project.br.useAuthentication.exception.AuthenticationExceptionResponse;
import project.br.useAuthentication.exception.BadRequestExceptionResult;
import project.br.useAuthentication.exception.InternalExceptionResult;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.AuthJPA;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.repository.AuthRepository;
import project.br.useAuthentication.util.CookieUtil;
import project.br.useAuthentication.util.JwtUtil;

@Service
public class AuthenticationService implements UserDetailsService{
	
	@Value("${security.jwt.tokenName}")
	private String accessToken;
	@Value("${security.jwt.refreshName}")
	private String refreshToken;
	@Autowired
	private JwtUtil jwtService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthRepository authRepository;
	@Autowired
	private TokenService tokenService;

	public StatusResult<?> authenticate(AuthJPA auth) {
		final AuthJPA authDB;
		try {
			if (auth.getEmail() != null) {
				authDB = this.authRepository.findBy_email(auth.getEmail()).orElseThrow(
						() -> new UsernameNotFoundException("User not Found: " + auth.getEmail())
				);
			}
			else if (auth.getEmail() == null) {
				authDB = this.authRepository.findBy_username(auth.getUsername()).orElseThrow(
						() -> new UsernameNotFoundException("User not Found: " + auth.getEmail())
				);
			}
			else {
				throw new UsernameNotFoundException("User not Found");
			}
			Boolean valid = this.passwordEncoder.matches(auth.getPassword(), authDB.getPassword());
			if(!valid) {
				throw new UsernameNotFoundException("Incorrect Email or Password , try again.");
			}
			String jwtToken = jwtService.generateToken(authDB, TokenType.ACCESS_TOKEN);
			String refreshToken = jwtService.generateToken(authDB, TokenType.REFRESH_TOKEN);
			response.setContentType(null);
			TokenJPA jwt = new TokenJPA(jwtToken, authDB);
			this.tokenService.removeByAuthID(authDB.getId());
			this.tokenService.insertUpdate(jwt);
			CookieUtil.create(response, this.accessToken, jwtToken, false, "localhost");
			CookieUtil.create(response, this.refreshToken, refreshToken, false, "localhost");
			return new StatusResult<Long>(HttpStatus.OK.value(), authDB.getId());
		}
		catch (Exception e) {
			throw new UsernameNotFoundException(e.getLocalizedMessage());
		}
	}

	@Override
	public AuthJPA loadUserByUsername(String username) {
		AuthJPA user = this.authRepository.findBy_username(username).orElseThrow(
			() -> new UsernameNotFoundException("User not Found: " + username)
		);
		return user;
	}
	
	public StatusResult<?> getAuthenticatedUserID(){
		final Cookie cookieAccess = WebUtils.getCookie(request, this.accessToken);
		final String accessToken = (cookieAccess != null) ? cookieAccess.getValue() : null;
		final TokenJPA jwt = this.tokenService.findByToken(accessToken);
		final String userID = jwtService.extractSubject(jwt.getToken()).orElseThrow(
			() -> new AuthenticationExceptionResponse(JwtType.EXPIRED_AT.toString()) 
		);
		return new StatusResult<Long>(HttpStatus.OK.value(), Long.parseLong(userID));
	}
	
	public StatusResult<?> authInsert(AuthJPA auth){
		try {			
			auth.setPassword(this.passwordEncoder.encode(auth.getPassword()));
			this.authRepository.save(auth);
			return new StatusResult<AuthJPA>(HttpStatus.OK.value(), auth);
		}
		catch(Exception e) {
			throw new InternalExceptionResult("Somenthing went wrong at insert Auth");
		}
	}
	
	public StatusResult<?> authUpdate(AuthJPA auth){
		try {
			if(this.tokenService.getTokenValidation(auth.getId()) == false) {
				throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
			}
			auth.setPassword(this.passwordEncoder.encode(auth.getPassword()));
			this.authRepository.save(auth);
			return new StatusResult<String>(HttpStatus.OK.value(), "Successfully!");
		}
		catch(Exception e) {
			throw new BadRequestExceptionResult("Somenthing went wrong at update Auth");
		}
	}
	
	public StatusResult<?> acceptAuth(AuthJPA auth){
		try {
			AuthJPA authDB = this.authRepository.findBy_email(auth.getEmail()).orElseThrow(
					() -> new UsernameNotFoundException("User not Found: " + auth.getEmail())
			);
			if(this.tokenService.getTokenValidation(authDB.getId()) == false) {
				throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
			}
			Boolean valid = this.passwordEncoder.matches(auth.getPassword(), authDB.getPassword());
			if(!valid) {
				throw new UsernameNotFoundException("Incorrect Email or Password , try again.");
			}
			response.setContentType(null);
			return new StatusResult<String>(HttpStatus.OK.value(), "Successfully!");
		}
		catch (Exception e) {
			throw new UsernameNotFoundException("Incorrect Email or Password , try again.");
		}
	}
	
	public StatusResult<?> refresh() {
		final Cookie cookieAccess = WebUtils.getCookie(request, this.accessToken);
		final String accessToken = (cookieAccess != null) ? cookieAccess.getValue() : null;
		final TokenJPA jwt = this.tokenService.findByToken(accessToken);
		final String expiredAcessToken = jwtService.extractSubject(jwt.getToken()).orElse(null);
		if (expiredAcessToken == null) {
			final Cookie cookieRefresh = WebUtils.getCookie(request, this.refreshToken);
			final String refreshToken = (cookieRefresh != null) ? cookieRefresh.getValue() : null;
			if (refreshToken == null) {
				throw new AuthenticationExceptionResponse(JwtType.INVALID_RT.toString());
			}
			final String authID = jwtService.extractSubject(refreshToken).orElseThrow(
				() -> new AuthenticationExceptionResponse(JwtType.EXPIRED_RT.toString())
			);
			AuthJPA authDB = this.authRepository.findById(Long.parseLong(authID)).orElseThrow(
					() -> new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString())
			);
			String jwtToken = jwtService.generateToken(authDB, TokenType.ACCESS_TOKEN);
			String jwtRefresh = jwtService.generateToken(authDB, TokenType.REFRESH_TOKEN);
			jwt.setToken(jwtToken);
			this.tokenService.insertUpdate(jwt);
			CookieUtil.create(response, this.accessToken, jwtToken, false, "localhost");
			CookieUtil.create(response, this.refreshToken, jwtRefresh , false, "localhost");
			return new StatusResult<String>(HttpStatus.OK.value(), "Refresh Succefully Done");
		}
		else {
			throw new AuthenticationExceptionResponse("Access Token not expired, also can't be refreshed");
		}
	}
	
	public StatusResult<?> logout() {
		try {
			final Cookie cookie = WebUtils.getCookie(this.request, this.accessToken);
			final String jwt = (cookie != null) ? cookie.getValue() : null;
			TokenJPA jwtDB = tokenService.findByToken(jwt);
		    CookieUtil.clear(response, this.accessToken);
		    CookieUtil.clear(response, this.refreshToken);
		    tokenService.remove(jwtDB.getId());
		    SecurityContextHolder.clearContext();
		    return new StatusResult<String>(HttpStatus.OK.value(), "LogOut Succefully Done");
		}
		catch(Exception e) {
			throw new AuthenticationExceptionResponse("LogOut not accepted");
		}
	}
}
