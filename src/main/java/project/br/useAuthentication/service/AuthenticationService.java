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
import project.br.useAuthentication.enumState.TokenType;
import project.br.useAuthentication.format.StatusResult;
import project.br.useAuthentication.jpaModel.TokenJPA;
import project.br.useAuthentication.repository.TokenRepository;
import project.br.useAuthentication.repository.UserRepository;
import project.br.useAuthentication.util.CookieUtil;
import project.br.useAuthentication.util.JwtUtil;

@Service
public class AuthenticationService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private JwtUtil jwtService;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpServletRequest request;
	@Value("${security.jwt.tokenName}")
	private String token;

	public StatusResult<?> register(UserDTO user) {
		this.userService.insertUpdate(user);
	    return new StatusResult<String>(HttpStatus.OK.value(), "Sing up was successfully made it.");
	}

	public StatusResult<?> authenticate(AuthDTO auth) {
		try {
			UserDTO user = this.userService.loadUserByEmail(auth.getEmail());
			Boolean valid = this.passwordEncoder.matches(auth.getPassword(), user.getPassword());
			if(!valid) {
				throw new Exception();
			}
			var jwtToken = jwtService.generateToken(user);
			var refreshToken = jwtService.generateToken(user);
			response.setContentType(null);
			//CookieUtil.create(response, this.token, jwtToken, false, -1, "localhost");
			//CookieUtil.create(response, "refreshToken", refreshToken, false, -1, "localhost");
			//revokeAllUserTokens(user);
			saveUserToken(user, jwtToken);
			return new StatusResult<String>(HttpStatus.OK.value(), user.getId().toString());
		}
		catch (Exception e) {
			throw new UsernameNotFoundException("Incorrect Email or Password , try again.");
		}
	}
	
	public StatusResult<?> refresh(){
		try {
			final Cookie cookie = WebUtils.getCookie(request, "token");
			final String accessToken = (cookie != null) ? cookie.getValue() : null;
			final TokenJPA jwt = this.tokenRepository.findByToken(accessToken).orElseThrow(
				() -> new Exception("Token Invalido")
			);
			/*if (jwt.isRevoked()) {
				invalidar todos os tokens do usuario
				throw new Exception("Token Invalido");
			}*/
			final String userID = jwtService.extractSubject(jwt.getToken()).orElse(null);
			
			if (userID == null) {
				var userID = this.tokenRepository.findUserIdByToken();			
				
				final Cookie cookie = WebUtils.getCookie(request, "refreshToken");
				final String refreshToken = (cookie != null) ? cookie.getValue() : null;
				if (refreshToken == null) {
					throw new Exception("tentou acessar sem ter um token");
				}
				final String userID = jwtService.extractSubject(refreshToken).orElseThrow(
						() -> new Exception("Expired RefreshToken!")
				);
				var u = this.userRepository.findById(Long.parseLong(userID)).orElseThrow(
						() -> new Exception("Refresh Token has Invalide User")
				);
				UserDTO user = new UserDTO(u);
				//invalidar access token no banco
				var jwtToken = jwtService.generateToken(user);
				var newRToken = jwtService.generateToken(user);
				CookieUtil.create(response, "token", jwtToken, false, -1, "localhost");
				CookieUtil.create(response, "refreshToken", newRToken , false, -1, "localhost");
				saveUserToken(user, jwtToken);
				return new StatusResult<String>(HttpStatus.OK.value(), user.getId().toString());
			}
			else {
				throw new Exception("Refresh token can't be used");
			}
		}
		catch(Exception e) {
			throw new UsernameNotFoundException("Incorrect Email or Password , try again.");
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
