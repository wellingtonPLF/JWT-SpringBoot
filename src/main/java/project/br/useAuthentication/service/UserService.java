package project.br.useAuthentication.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.repository.UserRepository;
import project.br.useAuthentication.util.JwtUtil;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private JwtUtil jwtService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PasswordEncoder encoder;
		
	public StatusResult<?> listAll() {
		try {
			List<UserJPA> result = this.userRepository.findAll();
			List<UserDTO> userlist = result.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
			return new StatusResult<List<UserDTO>>(HttpStatus.OK.value(), userlist);
		}
		catch(Exception e) {
			throw new InternalExceptionResult("Something Went Wrong!");
		}
	}
	
	public StatusResult<?> findById(Long id) {
		UserDTO user = new UserDTO(this.userRepository.findById(id).orElseThrow(
			() -> new NotFoundExceptionResult("The requested Id was not found.")));
		return new StatusResult<UserDTO>(HttpStatus.OK.value(), user);
	}
		
	@Override
	public UserJPA loadUserByUsername(String username) {
		UserJPA user = this.userRepository.findBy_username(username).orElseThrow(
			() -> new UsernameNotFoundException("User not Found: " + username)
		);
		return user;
	}

	@Transactional
	public StatusResult<?> insertUpdate(UserJPA user) {
		if (user == null) {
			throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
		}
		if (user.getId() != null) {
			if(this.getTokenValidation(user.getId()) == false) {
				throw new AuthenticationExceptionResponse(JwtType.INVALID_USER.toString());
			}
		}
		user.setPassword(this.encoder.encode(user.getPassword()));
		UserDTO u = new UserDTO(this.userRepository.save(user));
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
		final Cookie cookieAccess = WebUtils.getCookie(request, "token");
		final String accessToken = (cookieAccess != null) ? cookieAccess.getValue() : null;
		final TokenJPA jwt = this.tokenService.findByToken(accessToken);
		final String userID = jwtService.extractSubject(jwt.getToken()).orElseThrow();
		if (Long.parseLong(userID) == id) {
			return true;
		}
		return false;
	}
}
