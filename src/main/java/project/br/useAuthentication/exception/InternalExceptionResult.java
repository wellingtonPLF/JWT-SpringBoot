package project.br.useAuthentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalExceptionResult extends RuntimeException {
	
	private final static long serialVersionUID = 1L;
	
	public InternalExceptionResult (String message) {
		super(message);
	}
}
