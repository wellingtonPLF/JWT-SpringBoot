package project.br.useAuthentication.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.enumState.JwtType;
import project.br.useAuthentication.exception.AuthenticationExceptionResponse;
import project.br.useAuthentication.exception.BadRequestExceptionResult;
import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.AuthJPA;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.repository.AuthRepository;
import project.br.useAuthentication.repository.UserRepository;

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
	
	@Transactional
	public StatusResult<?> listAll() {
		List<UserJPA> userDB = this.userRepository.findAll();
		List<AuthJPA> authDB = this.authRepository.findAll();
		List<UserDTO> users = userDB.stream()
			.map(user ->
					new UserDTO(user, authDB.stream().filter((auth) -> auth.getId() == user.getAuth().getId()).findFirst()
							.orElse(null))
				).collect(Collectors.toList());
		return new StatusResult<List<UserDTO>>(HttpStatus.OK.value(), users);
	}
	
	public StatusResult<?> findById(Long id) {
		try {
			UserJPA userDB = this.userRepository.findById(id).orElse(null);
			AuthJPA authDB = this.authRepository.findByUserID(userDB.getId()).orElseThrow();
			UserDTO user = new UserDTO(userDB, authDB);
			return new StatusResult<UserDTO>(HttpStatus.OK.value(), user);
		}
		catch(Exception e) {
			throw new NotFoundExceptionResult("The requested Id was not found.");
		}
	}

	public StatusResult<?> insert(UserJPA user) {
		try {
			UserJPA userDB = this.userRepository.save(user);
			AuthJPA authDB = this.authRepository.findByUserID(user.getId()).orElseThrow();
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
		if(this.tokenService.getTokenValidation(user.getId()) == false) {
			throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
		}
		AuthJPA authDB = this.authRepository.findByUserID(user.getId()).orElseThrow();
		user.setAuth(authDB);
		UserJPA userDB = this.userRepository.save(user); 
		UserDTO u = new UserDTO(userDB, authDB);
		return new StatusResult<UserDTO>(HttpStatus.OK.value(), u);
	}
	
	public StatusResult<?> remove(Long id) {
		if (id == null) {
			throw new BadRequestExceptionResult("UserId is null");
		}
		if(this.tokenService.getTokenValidation(id) == false) {
			throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
		}
		try {
			AuthJPA auth = this.authRepository.findByUserID(id).orElseThrow();
			this.authRepository.deleteById(auth.getId());;
			return new StatusResult<HttpStatus>(HttpStatus.OK.value(), HttpStatus.OK);
		}
		catch(Exception e) {
			throw new NotFoundExceptionResult("The requested Id was not found.");
		}
	}
	
	
}
