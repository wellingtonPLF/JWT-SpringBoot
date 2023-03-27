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

import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.exception.InternalExceptionResult;
import project.br.useAuthentication.exception.NotFoundExceptionResult;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
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
		UserDTO user = new UserDTO(this.userRepository.findById(id).orElseThrow(() -> 
			new NotFoundExceptionResult("The requested Id was not found.")));
		return new StatusResult<UserDTO>(HttpStatus.OK.value(), user);
	}
	
	public UserJPA loadUserByEmail(String email) {
		UserJPA user = this.userRepository.findBy_email(email).orElseThrow(
				() -> new UsernameNotFoundException("User not Found: " + email)
		);
		return user;
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
		if (user.getPassword() != null) {
			if (user.getPassword().length() < 8) {
				throw new IllegalArgumentException("Password Not Valid");
			}
		}
		user.setPassword(this.encoder.encode(user.getPassword()));
		UserDTO u = new UserDTO(this.userRepository.save(user));
		return new StatusResult<UserDTO>(HttpStatus.OK.value(), u);			

	}
	
	public StatusResult<?> remove(Long id) {
		try {
			this.userRepository.deleteById(id);
			return new StatusResult<HttpStatus>(HttpStatus.OK.value(), HttpStatus.OK);
		}
		catch(Exception e) {
			throw new NotFoundExceptionResult("The requested Id was not found.");
		}
	}
}
