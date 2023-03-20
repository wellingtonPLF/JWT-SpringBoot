package project.br.useAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import project.br.useAuthentication.dtoModel.AuthDTO;
import project.br.useAuthentication.dtoModel.TokenDTO;
import project.br.useAuthentication.dtoModel.UserDTO;
import project.br.useAuthentication.enumState.TokenType;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.repository.TokenRepository;
import project.br.useAuthentication.util.CookieUtil;

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
	@Autowired
	private HttpServletResponse httpServletResponse;
	@Value("${security.jwt.tokenName}")
	private String token;

	public StatusResult<?> register(UserDTO user) {
		this.userService.insertUpdate(user);
	    return new StatusResult<String>(HttpStatus.OK.value(), "Sing Up was Successfully made it.");
	}

	public StatusResult<?> authenticate(AuthDTO auth) {
		try {
			UserDTO user = this.userService.loadUserByEmail(auth.getEmail());
			Boolean valid = this.passwordEncoder.matches(auth.getPassword(), user.getPassword());
			if(!valid) {
				throw new Exception();
			}
			var jwtToken = jwtService.generateToken(user);
			CookieUtil.create(httpServletResponse, this.token, jwtToken, false, -1, "localhost");
			revokeAllUserTokens(user);
			saveUserToken(user, jwtToken);
			return new StatusResult<String>(HttpStatus.OK.value(), user.getId().toString());
		}
		catch (Exception e) {
			throw new UsernameNotFoundException("Incorrect Email or Password , try again." + auth.getPassword());
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
