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
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.enumState.JwtType;
import project.br.useAuthentication.exception.AuthenticationExceptionResponse;
import project.br.useAuthentication.exception.BadRequestExceptionResult;
import project.br.useAuthentication.exception.InternalExceptionResult;
import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.AuthJPA;
import project.br.useAuthentication.jpaModel.RoleJPA;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.repository.AuthRepository;
import project.br.useAuthentication.repository.UserRepository;
import project.br.useAuthentication.util.JwtUtil;

@Service
public class UserService {
	
	@Value("${security.jwt.tokenName}")
	private String accessToken;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthRepository authRepository;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private JwtUtil jwtService;
	@Autowired
	private HttpServletRequest request;
	
	public StatusResult<?> listAll() {
		try {
			List<UserJPA> userDB = this.userRepository.findAll();
			List<AuthJPA> authDB = this.authRepository.findAll();
			List<UserDTO> users = userDB.stream()
				.map(user ->
						new UserDTO(user, authDB.stream().filter((auth) -> auth.getId() == user.getAuth().getId()).findFirst()
								.orElse(null))
					).collect(Collectors.toList());
			return new StatusResult<List<UserDTO>>(HttpStatus.OK.value(), users);
		}
		catch(Exception e) {
			throw new InternalExceptionResult("Something Went Wrong!");
		}
	}
	
	public StatusResult<?> findById(Long id) {
		try {
			UserJPA userDB = this.userRepository.findById(id).orElseThrow();
			AuthJPA authDB = this.authRepository.findByUserID(userDB.getId()).orElseThrow();
			UserDTO user = new UserDTO(userDB, authDB);
			return new StatusResult<UserDTO>(HttpStatus.OK.value(), user);
		}
		catch(Exception e) {
			throw new NotFoundExceptionResult("The requested Id was not found.");
		}
	}

	public StatusResult<?> insert(UserJPA user) {
		if (user == null) {
			throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
		}
		UserJPA userDB = this.userRepository.save(user);
		AuthJPA authDB = this.authRepository.findByUserID(user.getId()).orElseThrow();
		UserDTO u = new UserDTO(userDB, authDB);
		return new StatusResult<UserDTO>(HttpStatus.OK.value(), u);
	}

	@Transactional
	public StatusResult<?> update(UserJPA user) {
		if (user == null) {
			throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
		}
		if(this.getTokenValidation(user.getId()) == false) {
			throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
		}
		UserJPA userDB = this.userRepository.save(user); 
		AuthJPA authDB = this.authRepository.findByUserID(user.getId()).orElseThrow();
		UserDTO u = new UserDTO(userDB, authDB);
		return new StatusResult<UserDTO>(HttpStatus.OK.value(), u);
	}
	
	public StatusResult<?> remove(Long id) {
		if (id == null) {
			throw new BadRequestExceptionResult("UserId is null");
		}
		if(this.getTokenValidation(id) == false) {
			throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
		}
		try {
			this.userRepository.deleteById(id);
			return new StatusResult<HttpStatus>(HttpStatus.OK.value(), HttpStatus.OK);
		}
		catch(Exception e) {
			throw new NotFoundExceptionResult("The requested Id was not found.");
		}
	}
	
	public Boolean getTokenValidation(Long id) {
		final long admin = 1;
		final Cookie cookieAccess = WebUtils.getCookie(request, this.accessToken);
		final String accessToken = (cookieAccess != null) ? cookieAccess.getValue() : null;
		final TokenJPA jwt = this.tokenService.findByToken(accessToken);
		final String authID = jwtService.extractSubject(jwt.getToken()).orElseThrow();
		final AuthJPA auth = this.authRepository.findById(Long.parseLong(authID)).orElseThrow();
		final Long result = auth.getRoles().stream().map(RoleJPA::getId).filter(x -> x == admin).findFirst().orElse(null);
		if (Long.parseLong(authID) == id) {
			return true;
		}
		else if (result != null) {
			return true;
		}
		return false;
	}
}
