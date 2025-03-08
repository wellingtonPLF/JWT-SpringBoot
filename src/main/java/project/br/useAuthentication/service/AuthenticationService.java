package project.br.useAuthentication.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.br.useAuthentication.enumState.JwtType;
import project.br.useAuthentication.enumState.TokenType;
import project.br.useAuthentication.exception.AuthenticationExceptionResponse;
import project.br.useAuthentication.exception.InternalExceptionResult;
import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.AuthJPA;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.repository.AuthRepository;
import project.br.useAuthentication.util.CookieUtil;
import project.br.useAuthentication.util.JwtUtil;

@Service
public class AuthenticationService implements UserDetailsService{
	
	@Value("${security.jwt.tokenName}")
	private String accessTokenName;
	@Value("${security.jwt.refreshName}")
	private String refreshTokenName;
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
			CookieUtil.create(response, this.accessTokenName, jwtToken, request);
			CookieUtil.create(response, this.refreshTokenName, refreshToken, request);
			return new StatusResult<String>(HttpStatus.OK.value(), "Success!");
		}
		catch (Exception e) {
			throw new UsernameNotFoundException(e.getLocalizedMessage());
		}
	}
	
	// ------------------------------------------------------------------------------------------

	public List<AuthJPA> findAll(){
		return this.authRepository.findAll();
	}
	
	public Boolean isLogged(){
		String jwt = CookieUtil.getCookieValue(this.request, this.accessTokenName);
		TokenJPA jwtDB;
		try {
			jwtDB = this.tokenService.findByToken(jwt);
		}
		catch(Exception e) {
			return false;
		}
		jwtService.extractSubject(jwtDB.getToken()).orElseThrow(
			() -> new AuthenticationExceptionResponse(JwtType.EXPIRED_AT.toString()) 
		);
		return true;
	}
	
	
	public AuthJPA findById(String authID) {
		return this.authRepository.findById(Long.parseLong(authID)).orElseThrow(
			() -> new NotFoundExceptionResult("The requested Id was not found.")
		);
	}
	
	public AuthJPA findByUserID(Long id) {
		AuthJPA authDB = this.authRepository.findByUserID(id).orElseThrow(
			() -> new NotFoundExceptionResult("The requested Id was not found.")
		);
		return authDB; 
	}
	
	public void deleteById(Long id) {
		this.authRepository.deleteById(id);
	}
	
	@Override
	public AuthJPA loadUserByUsername(String username) {
		AuthJPA user = this.authRepository.findBy_username(username).orElseThrow(
			() -> new UsernameNotFoundException("User not Found: " + username)
		);
		return user;
	}
	
	// ------------------------------------------------------------------------------------------
	
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
		final String accessToken = CookieUtil.getCookieValue(this.request, this.accessTokenName);
		final TokenJPA jwtDB = this.tokenService.findByToken(accessToken);
		final String authID = jwtService.extractSubject(jwtDB.getToken()).orElseThrow(
			() -> new AuthenticationExceptionResponse(JwtType.EXPIRED_AT.toString())
		);
		AuthJPA authDB = this.authRepository.findById(Long.parseLong(authID)).orElseThrow(
				() -> new UsernameNotFoundException("User not Found: " + auth.getEmail())
		);
		authDB.setPassword(this.passwordEncoder.encode(auth.getPassword()));
		if (auth.getEmail() != null) {
			authDB.setEmail(auth.getEmail());
		}
		if (auth.getUsername() != null) {
			authDB.setUsername(auth.getUsername());
		}
		this.authRepository.save(authDB);
		return new StatusResult<String>(HttpStatus.OK.value(), "Successfully!");
	}
	
	public StatusResult<?> acceptAuth(AuthJPA auth){
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
	
	public StatusResult<?> refresh() {
		final String accessToken = CookieUtil.getCookieValue(this.request, this.accessTokenName);
		final TokenJPA jwt = this.tokenService.findByToken(accessToken);
		final String expiredAcessToken = jwtService.extractSubject(jwt.getToken()).orElse(null);
		if (expiredAcessToken == null) {
			final String refreshToken = CookieUtil.getCookieValue(this.request, this.refreshTokenName);
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
			CookieUtil.create(response, this.accessTokenName, jwtToken, request);
			CookieUtil.create(response, this.refreshTokenName, jwtRefresh , request);
			return new StatusResult<String>(HttpStatus.OK.value(), "Refresh Succefully Done");
		}
		else {
			throw new AuthenticationExceptionResponse("Access Token not expired, also can't be refreshed");
		}
	}
	
	public StatusResult<?> logout() {
		try {
			final String jwt = CookieUtil.getCookieValue(this.request, this.accessTokenName);
			TokenJPA jwtDB = tokenService.findByToken(jwt);
			CookieUtil.clear(response, this.accessTokenName, request);
		    CookieUtil.clear(response, this.refreshTokenName, request);
		    SecurityContextHolder.clearContext();
		    tokenService.remove(jwtDB.getId());
		    return new StatusResult<String>(HttpStatus.OK.value(), "LogOut Succefully Done");
		}
		catch(Exception e) {
			throw new AuthenticationExceptionResponse("LogOut not accepted");
		}
	}
}
