package project.br.useAuthentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationExceptionResponse extends AuthenticationException{
	private final static long serialVersionUID = 1L;
	
	public AuthenticationExceptionResponse (String message) {
		super(message);
	}
}
