package project.br.useAuthentication.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundExceptionResult extends RuntimeException {
	
	private final static long serialVersionUID = 1L;
	
	public NotFoundExceptionResult(String exception) {
		super(exception);
	}
}