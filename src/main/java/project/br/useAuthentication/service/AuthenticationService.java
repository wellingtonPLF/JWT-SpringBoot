package project.br.useAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.br.useAuthentication.dtoModel.AuthDTO;
import project.br.useAuthentication.dtoModel.TokenDTO;
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.enumState.TokenType;
import project.br.useAuthentication.exception.UnAuthorizedExceptionResult;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.jpaModel.UserJPA;
import project.br.useAuthentication.repository.TokenRepository;

@Service
public class AuthenticationService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private JwtService jwtService;

	public StatusResult<?> register(UserDTO request) {
		this.userService.insertUpdate(request);
	    return new StatusResult<String>(HttpStatus.OK.value(), "Sing Up was Successfully made it.");
	}

	public StatusResult<?> authenticate(AuthDTO request) {
		try {
			UserDTO user = this.userService.loadUserByEmail(request.getEmail());
			Boolean valid = this.passwordEncoder.matches(request.getPassword(), user.getPassword());
			if(!valid) {
				throw new Exception();
			}
			var jwtToken = jwtService.generateToken(user);
			revokeAllUserTokens(user);
			saveUserToken(user, jwtToken);
			return new StatusResult<String>(HttpStatus.OK.value(), jwtToken);
		}
		catch (Exception e) {
			throw new UnAuthorizedExceptionResult("Auth: Incorret Email or Password , try again.");
		}
	}

	private void saveUserToken(UserDTO user, String jwtToken) {
		var token = new TokenDTO(user, jwtToken, TokenType.BEARER, false, false);
		tokenRepository.save(new TokenJPA(token));
	}

	private void revokeAllUserTokens(UserDTO user) {
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
