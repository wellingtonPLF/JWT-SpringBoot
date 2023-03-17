package project.br.useAuthentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ACCEPTED)
public class RefreshTokenException extends RuntimeException {
	private final static long serialVersionUID = 1L;
	
	public RefreshTokenException (String message) {
		super(message);
	}
}
