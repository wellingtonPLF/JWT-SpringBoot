package project.br.useAuthentication.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.enumState.JwtType;
import project.br.useAuthentication.exception.AuthenticationExceptionResponse;
import project.br.useAuthentication.exception.BadRequestExceptionResult;
import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.AuthJPA;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.repository.UserRepository;
import project.br.useAuthentication.util.CookieUtil;
import project.br.useAuthentication.util.JwtUtil;

@Service
public class UserService {
	
	@Value("${security.jwt.tokenName}")
	private String accessTokenName;
	@Value("${security.jwt.refreshName}")
	private String refreshTokenName;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthenticationService authService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private JwtUtil jwtService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	@Transactional
	public StatusResult<?> listAll() {
		List<UserJPA> userDB = this.userRepository.findAll();
		List<AuthJPA> authDB = this.authService.findAll();
		List<UserDTO> users = userDB.stream()
			.map(user ->
					new UserDTO(user, authDB.stream().filter(
							(auth) -> auth.getId() == user.getAuth().getId()).findFirst().orElse(null))
				).collect(Collectors.toList());
		return new StatusResult<List<UserDTO>>(HttpStatus.OK.value(), users);
	}
	
	public StatusResult<?> getAuthenticatedUser() {
		final String accessToken = CookieUtil.getCookieValue(this.request, this.accessTokenName);
		final TokenJPA jwt = this.tokenService.findByToken(accessToken);
		final String authID = jwtService.extractSubject(jwt.getToken()).orElseThrow(
			() -> new AuthenticationExceptionResponse(JwtType.EXPIRED_AT.toString()) 
		);
		AuthJPA authDB = this.authService.findById(authID);
		UserJPA userDB = this.userRepository.findBy_auth_id(authDB.getId()).orElseThrow();
		UserDTO user = new UserDTO(userDB, authDB);
		return new StatusResult<UserDTO>(HttpStatus.OK.value(), user);
	}

	public StatusResult<?> insert(UserJPA user) {
		try {
			UserJPA userDB = this.userRepository.save(user);
			AuthJPA authDB = this.authService.findByUserID(user.getId());
			UserDTO u = new UserDTO(userDB, authDB);
			return new StatusResult<UserDTO>(HttpStatus.OK.value(), u);
		}
		catch(Exception e) {
			throw new BadRequestExceptionResult("Something went wrong at insert User");
		}
	}

	@Transactional
	public StatusResult<?> update(UserJPA user) {
		if (user == null) {
			throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
		}
		AuthJPA authDB = this.authService.findByUserID(user.getId());
		if(this.tokenService.getTokenValidation(authDB.getId()) == false) {
			throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
		}
		user.setAuth(authDB);
		UserJPA userDB = this.userRepository.save(user); 
		UserDTO u = new UserDTO(userDB, authDB);
		return new StatusResult<UserDTO>(HttpStatus.OK.value(), u);
	}
	
	public StatusResult<?> remove(Long id) {
		if (id == null) {
			throw new BadRequestExceptionResult("UserId is null");
		}
		AuthJPA auth = this.authService.findByUserID(id);
		if(this.tokenService.getTokenValidation(auth.getId()) == false) {
			throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
		}
		this.authService.deleteById(auth.getId());
		CookieUtil.clear(this.response, this.accessTokenName, request);
	    CookieUtil.clear(this.response, this.refreshTokenName, request);
		return new StatusResult<HttpStatus>(HttpStatus.OK.value(), HttpStatus.OK);
	}	
}
