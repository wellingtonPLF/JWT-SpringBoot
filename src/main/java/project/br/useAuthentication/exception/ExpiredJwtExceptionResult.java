package project.br.useAuthentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredJwtExceptionResult extends RuntimeException {
private final static long serialVersionUID = 1L;
	
	public ExpiredJwtExceptionResult (String message) {
		super(message);
	}
}
